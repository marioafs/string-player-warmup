// --- Application State ---
// fallback to English if nothing is saved yet
let CURRENT_LANG = localStorage.getItem('app_lang') || 'en';
let uiTexts = {};
let sessionData = null;
let currentExerciseIndex = 0;
let timerInterval = null;
let isRoutineRunning = false;

// --- DOM Elements ---
let screenStart, screenInstructions, screenOverview, screenRoutine, screenEnd;

document.addEventListener("DOMContentLoaded", async () => {
    // highlight active toggle on boot
    updateLanguageButtonsUI();

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
        isRoutineRunning = true;
        // lock language toggle once session begins
        toggleLanguageSelector(false);

        const ambientAudio = document.getElementById('ambient-audio');
        if (ambientAudio) {
            ambientAudio.volume = 0.4; 
            ambientAudio.play().catch(e => console.log("Autoplay blocked:", e));
        }

        currentExerciseIndex = 0;
        showScreen(screenRoutine);
        startExerciseCycle();
    });

    // Setup End Screen Actions
    document.getElementById('btn-repeat').addEventListener('click', () => {
        isRoutineRunning = false;
        // unlock language selector on repetition
        toggleLanguageSelector(true);
        showScreen(screenOverview);
    });

    document.getElementById('btn-exit').addEventListener('click', () => {
        // stop background audio
        const ambientAudio = document.getElementById('ambient-audio');
        if (ambientAudio) {
            ambientAudio.pause();
            ambientAudio.currentTime = 0;
        }

        // attempt to close window
        window.open('', '_self', '');
        window.close();

        // graceful fallback if browser prevents script from closing the tab
        setTimeout(() => {
            const goodbyeMsg = CURRENT_LANG === 'pt' 
                ? "Sessão concluída. Podes fechar este separador!" 
                : "Session complete. You may close this tab!";
                
            document.body.innerHTML = `
                <div style="
                    display: flex; 
                    flex-direction: column; 
                    align-items: center; 
                    justify-content: center; 
                    height: 100vh; 
                    background: #0f172a; 
                    color: #f8fafc; 
                    font-family: sans-serif;
                    text-align: center;
                    padding: 20px;">
                    <h2 style="font-size: 1.8rem; margin-bottom: 10px;">🎻✨</h2>
                    <p style="font-size: 1.2rem; opacity: 0.9;">${goodbyeMsg}</p>
                </div>
            `;
        }, 150);
    });

    // Boot complete: Show first screen
    showScreen(screenStart);
});

// --- Language Selector ---

async function setLanguage(lang) {
    const selector = document.querySelector('.lang-selector');
    if ((selector && selector.classList.contains('hidden')) || CURRENT_LANG === lang) return;

    CURRENT_LANG = lang;
    localStorage.setItem('app_lang', lang);
    updateLanguageButtonsUI();

    await Promise.all([fetchUITexts(), fetchWarmupData()]);
    applyTranslations();
    populateOverviewGrid();
}

function updateLanguageButtonsUI() {
    document.querySelectorAll('.lang-btn').forEach(btn => btn.classList.remove('active'));
    const activeBtn = document.getElementById(`btn-lang-${CURRENT_LANG}`);
    if (activeBtn) {
        activeBtn.classList.add('active');
    }
}

function toggleLanguageSelector(visible) {
    const selector = document.querySelector('.lang-selector');
    if (!selector) return;

    if (visible) {
        selector.classList.remove('hidden');
    } else {
        selector.classList.add('hidden');
    }
}

// --- API Calls ---

async function fetchUITexts() {
    try {
        const response = await fetch(`/api/ui?lang=${CURRENT_LANG}`);
        uiTexts = await response.json();
    } catch (e) { 
        console.error("Error loading UI texts:", e); 
    }
}

async function fetchWarmupData() {
    try {
        const response = await fetch(`/api/warmup?lang=${CURRENT_LANG}`);
        sessionData = await response.json();
    } catch (e) { 
        console.error("Error loading Warmup data:", e); 
    }
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
    if (!grid) return;
    grid.innerHTML = '';
    
    if (sessionData && sessionData.routine) {
        sessionData.routine.forEach((ex, idx) => {
            const displayName = ex.categoryName ? ex.categoryName : `Category ${idx + 1}`;
            
            grid.innerHTML += `
                <div class="overview-item">
                    <img src="https://img.youtube.com/vi/${ex.youtubeId}/hqdefault.jpg" alt="thumbnail">
                    <p style="font-weight: bold; margin-top: 5px;">${displayName}</p>
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
    if (!sessionData || currentExerciseIndex >= sessionData.routine.length) {
        showScreen(screenEnd);
        return;
    }

    const currentExercise = sessionData.routine[currentExerciseIndex];
    
    document.getElementById('category-title').innerText = currentExercise.categoryName;
    document.getElementById('video-objective').innerText = currentExercise.videoObjective;
    
    const videoTitleElem = document.getElementById('video-title');
    if (videoTitleElem) videoTitleElem.style.display = 'none'; 
    
    const iframe = document.getElementById('youtube-player');
    const youtubeUrl = `https://www.youtube.com/embed/${currentExercise.youtubeId}?autoplay=1&mute=1&controls=0&disablekb=1&fs=0&modestbranding=1&rel=0&iv_load_policy=3&loop=1&playlist=${currentExercise.youtubeId}`;
    
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
    document.getElementById('video-container').className = "size-large";
    
    const infoPanel = document.getElementById('info-panel');
    infoPanel.style.opacity = "0"; 
    setTimeout(() => infoPanel.style.display = "none", 300);
    
    const phaseIndicator = document.getElementById('phase-indicator');
    const timerDisplay = document.getElementById('timer-display');
    const phraseDisplay = document.getElementById('motivational-phrase');
    
    phaseIndicator.innerText = uiTexts['ui.phase.active'] || "Active";
    phaseIndicator.style.color = "#4CAF50";
    timerDisplay.style.color = "#4CAF50";
    
    // show final phrase on last exercise, random phrase otherwise
    if (currentExerciseIndex === 4) {
        phraseDisplay.innerText = uiTexts['ui.phrase.final'] || "";
        phraseDisplay.style.display = "block"; 
        setTimeout(() => phraseDisplay.className = "phrase phrase-active", 50);
    } else {
        fetch(`/api/ui/phrase/random?lang=${CURRENT_LANG}`)
            .then(res => res.json())
            .then(data => {
                if (data.phrase) {
                    phraseDisplay.innerText = data.phrase;
                    phraseDisplay.style.display = "block"; 
                    setTimeout(() => phraseDisplay.className = "phrase phrase-active", 50);
                }
            })
            .catch(err => {
                console.error("Failed to load phrase:", err);
                phraseDisplay.innerText = CURRENT_LANG === 'en' 
                    ? "Keep your focus on your posture!" 
                    : "Mantém o foco na tua postura!";
                phraseDisplay.style.display = "block"; 
                setTimeout(() => phraseDisplay.className = "phrase phrase-active", 50);
            });
    }

    // restart video stream
    const currentExercise = sessionData.routine[currentExerciseIndex];
    const iframe = document.getElementById('youtube-player');
    const urlLimpa = `https://www.youtube.com/embed/${currentExercise.youtubeId}?autoplay=1&mute=1&controls=0&disablekb=1&modestbranding=1&fs=0&rel=0&iv_load_policy=3&loop=1&playlist=${currentExercise.youtubeId}`;
    
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