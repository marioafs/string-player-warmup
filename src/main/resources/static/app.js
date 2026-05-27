// --- Application State ---
const CURRENT_LANG = "pt"; // Change to "es" or "en" to test the system
let uiTexts = {};
let sessionData = null;
let currentExerciseIndex = 0;
let timerInterval = null;

// --- DOM Elements ---
let screenStart, screenInstructions, screenOverview, screenRoutine, screenEnd;

document.addEventListener("DOMContentLoaded", async () => {
    // 1. Initialize DOM Screens
    screenStart = document.getElementById('screen-start');
    screenInstructions = document.getElementById('screen-instructions');
    screenOverview = document.getElementById('screen-overview');
    screenRoutine = document.getElementById('screen-routine');
    screenEnd = document.getElementById('screen-end');

    // 2. Fetch all data simultaneously (UI Strings + Session Data)
    await Promise.all([fetchUITexts(), fetchWarmupData()]);

    // 3. Apply translations and populate views
    applyTranslations();
    populateOverviewGrid();

    // 4. Setup Free Navigation Logic (Screens 1, 2, 3)
    document.getElementById('btn-to-instructions').addEventListener('click', () => showScreen(screenInstructions));
    document.getElementById('btn-back-to-start').addEventListener('click', () => showScreen(screenStart));
    
    document.getElementById('btn-to-overview').addEventListener('click', () => showScreen(screenOverview));
    document.getElementById('btn-back-to-instructions').addEventListener('click', () => showScreen(screenInstructions));

    // 5. Setup Routine Triggers (Screen 3 -> 4)
    document.getElementById('btn-start-routine').addEventListener('click', () => {
        currentExerciseIndex = 0;
        showScreen(screenRoutine);
        startExerciseCycle();
    });

    // 6. Setup End Screen Actions
    document.getElementById('btn-repeat').addEventListener('click', () => showScreen(screenOverview)); // Loops back to step 3 as requested
    document.getElementById('btn-exit').addEventListener('click', () => {
        alert("Fechando a aplicação (Lógica JavaFX futura será invocada aqui).");
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
    // Use YouTube thumbnail API to show videos without loading 5 heavy iframes
    sessionData.routine.forEach((ex, idx) => {
        grid.innerHTML += `
            <div class="overview-item">
                <img src="https://img.youtube.com/vi/${ex.youtubeId}/hqdefault.jpg" alt="thumbnail">
                <p>${idx + 1}. ${ex.videoTitle}</p>
            </div>
        `;
    });
}

// --- Display Logic ---
function showScreen(screenElement) {
    document.querySelectorAll('.screen').forEach(s => s.classList.remove('active'));
    screenElement.classList.add('active');
}

// --- The Core Loop Logic (Screen 4) ---
function startExerciseCycle() {
    if (currentExerciseIndex >= sessionData.routine.length) {
        showScreen(screenEnd);
        return;
    }

    const currentExercise = sessionData.routine[currentExerciseIndex];
    
    // Update basic texts
    document.getElementById('category-title').innerText = currentExercise.categoryName;
    document.getElementById('video-title').innerText = currentExercise.videoTitle;
    document.getElementById('video-objective').innerText = currentExercise.videoObjective;
    
    // Mount Iframe
    const iframe = document.getElementById('youtube-player');
    iframe.src = `https://www.youtube.com/embed/${currentExercise.youtubeId}?autoplay=1&mute=1&controls=0&loop=1&playlist=${currentExercise.youtubeId}`;
    
    startRestPhase();
}

function startRestPhase() {
    // 1. Encolhe o vídeo e traz as instruções de volta ao layout
    document.getElementById('video-container').className = "size-medium";
    
    const infoPanel = document.getElementById('info-panel');
    infoPanel.style.display = "block"; // Devolve o espaço físico ao painel
    setTimeout(() => infoPanel.style.opacity = "1", 50); // Faz o fade in suave
    
    // 2. Esconde a frase do ecrã
    const phraseDisplay = document.getElementById('motivational-phrase');
    phraseDisplay.className = "phrase"; 
    setTimeout(() => phraseDisplay.style.display = "none", 300); // Remove o espaço físico
    
    const phaseIndicator = document.getElementById('phase-indicator');
    const timerDisplay = document.getElementById('timer-display');
    
    phaseIndicator.innerText = uiTexts['ui.phase.prep']; 
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
    // 1. Cresce o vídeo e apaga o painel de instruções do layout (liberta espaço!)
    document.getElementById('video-container').className = "size-large";
    
    const infoPanel = document.getElementById('info-panel');
    infoPanel.style.opacity = "0"; 
    setTimeout(() => infoPanel.style.display = "none", 300); // Tira o painel do caminho
    
    const phaseIndicator = document.getElementById('phase-indicator');
    const timerDisplay = document.getElementById('timer-display');
    const phraseDisplay = document.getElementById('motivational-phrase');
    
    phaseIndicator.innerText = uiTexts['ui.phase.active'];
    phaseIndicator.style.color = "#4CAF50";
    timerDisplay.style.color = "#4CAF50";
    
    // 2. Lógica exata do PDF: A instrução de fechar os olhos SÓ entra no exercício 5 (index 4)
    if (currentExerciseIndex === 4) {
        phraseDisplay.innerText = uiTexts['ui.phrase.final'];
    } else {
        phraseDisplay.innerText = sessionData.globalMotivationalPhrase;
    }
    
    // 3. Mostra a frase no ecrã!
    phraseDisplay.style.display = "block"; // Reserva o espaço físico
    setTimeout(() => phraseDisplay.className = "phrase phrase-active", 50); // Faz a animação
    
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