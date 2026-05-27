// --- Application State ---
const CURRENT_LANG = "pt"; // Change to "es" or "en" to test the system
let uiTexts = {};
let sessionData = null;
let currentExerciseIndex = 0;
let timerInterval = null;

// --- DOM Elements ---
let screenStart, screenInstructions, screenOverview, screenRoutine, screenEnd;

document.addEventListener("DOMContentLoaded", async () => {
    // Initialize DOM Screens
    screenStart = document.getElementById('screen-start');
    screenInstructions = document.getElementById('screen-instructions');
    screenOverview = document.getElementById('screen-overview');
    screenRoutine = document.getElementById('screen-routine');
    screenEnd = document.getElementById('screen-end');

    // Fetch all data simultaneously (UI Strings + Session Data)
    await Promise.all([fetchUITexts(), fetchWarmupData()]);

    // Apply translations and populate views
    applyTranslations();
    populateOverviewGrid();

    // Setup Free Navigation Logic (Screens 1, 2, 3)
    document.getElementById('btn-to-instructions').addEventListener('click', () => showScreen(screenInstructions));
    document.getElementById('btn-back-to-start').addEventListener('click', () => showScreen(screenStart));
    
    document.getElementById('btn-to-overview').addEventListener('click', () => showScreen(screenOverview));
    document.getElementById('btn-back-to-instructions').addEventListener('click', () => showScreen(screenInstructions));

    // Setup Routine Triggers (Screen 3 -> 4)
    document.getElementById('btn-start-routine').addEventListener('click', () => {
        const ambientAudio = document.getElementById('ambient-audio');
        ambientAudio.volume = 0.4; 
        
        // Handle play() promise rejection due to browser autoplay policies
        ambientAudio.play().catch(e => console.log("O Chrome bloqueou o autoplay do áudio:", e));

        currentExerciseIndex = 0;
        showScreen(screenRoutine);
        startExerciseCycle();
    });

    // Setup End Screen Actions
    document.getElementById('btn-repeat').addEventListener('click', () => showScreen(screenOverview)); 
    document.getElementById('btn-exit').addEventListener('click', () => {
        // Stop audio playback immediately
        const ambientAudio = document.getElementById('ambient-audio');
        if (ambientAudio) {
            ambientAudio.pause();
            ambientAudio.currentTime = 0;
        }

        // Trigger backend shutdown
        fetch('/api/ui/shutdown', { method: 'POST' })
            .catch(err => console.log("a fechar..."));

        // Force window close by bypassing standard browser restrictions
        setTimeout(() => {
            window.open('', '_self', '');
            window.close();
            
            // Fallback: Black out the screen if the window refuses to close
            document.body.innerHTML = '<div style="background:#000; width:100vw; height:100vh;"></div>';
        }, 100);
    });

    // Boot complete: Show first screen
    showScreen(screenStart);
});

// --- API Calls ---
async function fetchUITexts() {
    try {
        const response = await fetch(`http://localhost:8080/api/ui?lang=${CURRENT_LANG}`);
        uiTexts = await response.json();
    } catch (e) { console.error("Error loading UI texts:", e); }
}

async function fetchWarmupData() {
    try {
        const response = await fetch(`http://localhost:8080/api/warmup?lang=${CURRENT_LANG}`);
        sessionData = await response.json();
    } catch (e) { console.error("Error loading Warmup data:", e); }
}

// --- Dynamic Injection ---
function applyTranslations() {
    document.querySelectorAll('[data-i18n]').forEach(element => {
        const key = element.getAttribute('data-i18n');
        if (uiTexts[key]) {
            element.innerText = uiTexts[key];
        }
    });
}

function populateOverviewGrid() {
    const grid = document.getElementById('overview-grid');
    grid.innerHTML = '';
    
    if (sessionData && sessionData.routine) {
        sessionData.routine.forEach((ex, idx) => {
            // Fallback for missing categoryName
            const nomeExibicao = ex.categoryName ? ex.categoryName : `Categoria ${idx + 1}`;
            
            grid.innerHTML += `
                <div class="overview-item">
                    <img src="https://img.youtube.com/vi/${ex.youtubeId}/hqdefault.jpg" alt="thumbnail">
                    <p style="font-weight: bold; margin-top: 5px;">${nomeExibicao}</p>
                </div>
            `;
        });
    }
}

// --- Display Logic ---
function showScreen(screenElement) {
    document.querySelectorAll('.screen').forEach(s => s.classList.remove('active'));
    screenElement.classList.add('active');
}

function startExerciseCycle() {
    if (currentExerciseIndex >= sessionData.routine.length) {
        showScreen(screenEnd);
        return;
    }

    const currentExercise = sessionData.routine[currentExerciseIndex];
    
    // Update texts
    document.getElementById('category-title').innerText = currentExercise.categoryName;
    document.getElementById('video-objective').innerText = currentExercise.videoObjective;
    
    // Hide the empty video title element
    document.getElementById('video-title').style.display = 'none'; 
    
    // Mount Iframe with STRICT "no UI" parameters
    const iframe = document.getElementById('youtube-player');
    const youtubeUrl = `https://www.youtube.com/embed/${currentExercise.youtubeId}?autoplay=1&mute=1&controls=0&disablekb=1&fs=0&modestbranding=1&rel=0&iv_load_policy=3&loop=1&playlist=${currentExercise.youtubeId}`;
    
    // add a timestamp to force the browser to treat it as a new URL and reload the video
    iframe.src = youtubeUrl + `&t=${new Date().getTime()}`;
    
    startRestPhase();
}

function startRestPhase() {
    document.getElementById('video-container').className = "size-medium";
    
    const infoPanel = document.getElementById('info-panel');
    infoPanel.style.display = "block";
    setTimeout(() => infoPanel.style.opacity = "1", 50);
    
    const phraseDisplay = document.getElementById('motivational-phrase');
    phraseDisplay.className = "phrase"; 
    setTimeout(() => phraseDisplay.style.display = "none", 300);
    
    const phaseIndicator = document.getElementById('phase-indicator');
    const timerDisplay = document.getElementById('timer-display');
    
    phaseIndicator.innerText = uiTexts['ui.phase.prep'] || "Preparation"; 
    phaseIndicator.style.color = "#ff9800";
    timerDisplay.style.color = "#ff9800";
    
    let timeLeft = 10; 
    timerDisplay.innerText = timeLeft;
    
    clearInterval(timerInterval);
    timerInterval = setInterval(() => {
        timeLeft--;
        timerDisplay.innerText = timeLeft;
        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            startActivePhase();
        }
    }, 1000);
}

function startActivePhase() {
    // Expand video container
    document.getElementById('video-container').className = "size-large";
    
    const infoPanel = document.getElementById('info-panel');
    infoPanel.style.opacity = "0"; 
    setTimeout(() => infoPanel.style.display = "none", 300);
    
    const phaseIndicator = document.getElementById('phase-indicator');
    const timerDisplay = document.getElementById('timer-display');
    const phraseDisplay = document.getElementById('motivational-phrase');
    
    phaseIndicator.innerText = uiTexts['ui.phase.active'];
    phaseIndicator.style.color = "#4CAF50";
    timerDisplay.style.color = "#4CAF50";
    
    // DYNAMIC PHRASE LOGIC
    if (currentExerciseIndex === 4) { // the 5th exercise is the last
        phraseDisplay.innerText = uiTexts['ui.phrase.final'];
        phraseDisplay.style.display = "block"; 
        setTimeout(() => phraseDisplay.className = "phrase phrase-active", 50);
    } else {
        // Extract current language from URL or fallback to default
        const currentLang = new URLSearchParams(window.location.search).get('lang') || 'en';
        
        // Fetch phrase from backend
        fetch(`/api/ui/phrase/random?lang=pt`)
            .then(res => res.json())
            .then(data => {
                if(data.phrase) {
                    phraseDisplay.innerText = data.phrase;
                    phraseDisplay.style.display = "block"; 
                    // 50ms delay to ensure CSS transition applies smoothly
                    setTimeout(() => phraseDisplay.className = "phrase phrase-active", 50);
                }
            })
            .catch(err => {
                console.error("Erro a buscar a frase no backend:", err);
                // Fallback to prevent empty text display
                phraseDisplay.innerText = "Mantém o foco na tua postura!";
                phraseDisplay.style.display = "block"; 
                setTimeout(() => phraseDisplay.className = "phrase phrase-active", 50);
            });
    }

    // FORCE VIDEO RESTART
    const currentExercise = sessionData.routine[currentExerciseIndex];
    const iframe = document.getElementById('youtube-player');
    
    // Player params: hide controls, disable keyboard, hide logo, disable fullscreen, hide related videos
    const urlLimpa = `https://www.youtube.com/embed/${currentExercise.youtubeId}?autoplay=1&mute=1&controls=0&disablekb=1&modestbranding=1&fs=0&rel=0&iv_load_policy=3&loop=1&playlist=${currentExercise.youtubeId}`;
    
    // Append timestamp to force iframe reload
    iframe.src = urlLimpa + `&t=${new Date().getTime()}`;
    
    let timeLeft = 20; 
    timerDisplay.innerText = timeLeft;
    
    clearInterval(timerInterval);
    timerInterval = setInterval(() => {
        timeLeft--;
        timerDisplay.innerText = timeLeft;
        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            currentExerciseIndex++;
            startExerciseCycle();
        }
    }, 1000);
}