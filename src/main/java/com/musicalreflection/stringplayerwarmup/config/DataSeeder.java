package com.musicalreflection.stringplayerwarmup.config;

import com.musicalreflection.stringplayerwarmup.model.Category;
import com.musicalreflection.stringplayerwarmup.model.Phrase;
import com.musicalreflection.stringplayerwarmup.model.Video;
import com.musicalreflection.stringplayerwarmup.repository.CategoryRepository;
import com.musicalreflection.stringplayerwarmup.repository.PhraseRepository;
import com.musicalreflection.stringplayerwarmup.repository.VideoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VideoRepository videoRepository;
    private final PhraseRepository phraseRepository;

    public DataSeeder(CategoryRepository categoryRepository, 
                      VideoRepository videoRepository, 
                      PhraseRepository phraseRepository) {
        this.categoryRepository = categoryRepository;
        this.videoRepository = videoRepository;
        this.phraseRepository = phraseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            
            // create the 5 multilingual categories
            Category cat1 = categoryRepository.save(new Category(1, 
                "1. Hand Activation and Pulse", 
                "1. Ativação das Mãos e Pulsação", 
                "1. Activación de Manos y Pulso"));
                
            Category cat2 = categoryRepository.save(new Category(2, 
                "2. Bow Sliding (Bow Awareness)", 
                "2. Deslizamento na Vara (Consciência do Arco)", 
                "2. Deslizamiento en la Vara (Conciencia del Arco)"));
                
            Category cat3 = categoryRepository.save(new Category(3, 
                "3. Finger Flexion at Extremities", 
                "3. Flexão de Dedos nas Extremidades", 
                "3. Flexión de Dedos en los Extremos"));
                
            Category cat4 = categoryRepository.save(new Category(4, 
                "4. Weight Transfer and Fingerboard Mapping", 
                "4. Transferência de Peso e Mapeamento do Espelho", 
                "4. Transferencia de Peso y Mapeo del Diapasón"));
                
            Category cat5 = categoryRepository.save(new Category(5, 
                "5. Relaxation and Final Connection", 
                "5. Relaxamento e Conexão Final", 
                "5. Relajación y Conexión Final"));

            // insert 10 real videos (2 per category)
            
            // CAT 1
            videoRepository.save(new Video("eqFqKZj04kA", "", "", "",
                "Wake up hand muscles and internalize the pulse.", "Acordar a musculatura das mãos e interiorizar o pulso.", "Despertar la musculatura de las manos e interiorizar el pulso.", cat1));
            videoRepository.save(new Video("EhhWaSjoBiY", "", "", "",
                "Open and close hands following the rhythm.", "Abrir e fechar as mãos acompanhando o ritmo.", "Abrir y cerrar las manos siguiendo el ritmo.", cat1));

            // CAT 2
            videoRepository.save(new Video("6mpLuoGv-NQ", "", "", "",
                "Promote arm fluidity.", "Promover a fluidez dos braços.", "Promover la fluidez de los brazos.", cat2));
            videoRepository.save(new Video("a4UFqc6VpcM", "", "", "",
                "Feel bow weight and balance.", "Sentir o peso e o equilíbrio do arco.", "Sentir el peso y el equilibrio del arco.", cat2));

            // CAT 3
            videoRepository.save(new Video("R2_reN96Pdg", "", "", "",
                "Warm up finger joints.", "Aquecer as articulações dos dedos.", "Calentar las articulaciones de los dedos.", cat3));
            videoRepository.save(new Video("iiLExLHjiLM", "", "", "",
                "Gain flexibility for bow driving.", "Ganhar flexibilidade para a condução do arco.", "Ganar flexibilidad para la conducción del arco.", cat3));

            // CAT 4
            videoRepository.save(new Video("Pd5eE_FC-ng", "", "", "",
                "Feel arm weight surrender.", "Sentir a entrega do peso no braço direito.", "Sentir la entrega del peso en el brazo derecho.", cat4));
            videoRepository.save(new Video("I-diqmyxvno", "", "", "",
                "Internalize left arm distances.", "Interiorizar as distâncias geográficas do braço esquerdo.", "Interiorizar las distancias geográficas del brazo izquierdo.", cat4));

            // CAT 5
            videoRepository.save(new Video("WutO2fwlXh8", "", "", "",
                "Drop both hands relaxed.", "Deixar cair o peso de ambas as mãos relaxadamente.", "Dejar caer el peso de ambas manos relajadamente.", cat5));
            videoRepository.save(new Video("5BGmaXOzFyY", "", "", "",
                "Release tension and focus mind.", "Libertar tensões e focar a mente.", "Liberar tensiones y enfocar la mente.", cat5));

            // insert the phrases
            phraseRepository.saveAll(getFiftyMotivationalPhrases());

            System.out.println("Database populated!");
        }
    }

    private List<Phrase> getFiftyMotivationalPhrases() {
        List<Phrase> phrases = new ArrayList<>();
        
        phrases.add(new Phrase("Breathe deeply. Keep your eyes closed until the music ends.", "Respira profundamente. Mantém os olhos fechados até que a música termine.", "Respira profundamente. Mantén los ojos cerrados hasta que termine la música."));
        phrases.add(new Phrase("Keep your bow hand completely relaxed.", "Mantém a mão do arco completamente relaxada.", "Mantén la mano del arco completamente relajada."));
        phrases.add(new Phrase("Focus entirely on the purity of your sound.", "Foca-te inteiramente na pureza do teu som.", "Enfócate enteramente en la pureza de tu sonido."));
        phrases.add(new Phrase("Feel the resonance of the instrument.", "Sente a ressonância do instrumento.", "Siente la resonancia del instrumento."));
        phrases.add(new Phrase("Every challenge is a step towards perfection.", "Cada desafio é um passo em direção à perfeição.", "Cada desafío es un paso hacia la perfección."));
        phrases.add(new Phrase("Release the tension in your shoulders.", "Liberta a tensão nos teus ombros.", "Libera la tensión en tus hombros."));
        phrases.add(new Phrase("Let the weight of your arm do the work.", "Deixa que o peso do teu braço faça o trabalho.", "Deja que el peso de tu brazo haga el trabajo."));
        phrases.add(new Phrase("Listen to the silence between the notes.", "Ouve o silêncio entre as notas.", "Escucha el silencio entre las notas."));
        phrases.add(new Phrase("Your posture is the foundation of your tone.", "A tua postura é a base do teu som.", "Tu postura es la base de tu sonido."));
        phrases.add(new Phrase("Feel the connection between the string and the wood.", "Sente a conexão entre a corda e a madeira.", "Siente la conexión entre la cuerda y la madera."));
        
        phrases.add(new Phrase("Music flows where energy goes.", "A música flui para onde a energia vai.", "La música fluye hacia donde va la energía."));
        phrases.add(new Phrase("Keep your thumb curved and flexible.", "Mantém o polegar curvo e flexível.", "Mantén el pulgar curvo y flexible."));
        phrases.add(new Phrase("Allow your body to move naturally.", "Permite que o teu corpo se mova naturalmente.", "Permite que tu cuerpo se mueva naturalmente."));
        phrases.add(new Phrase("Patience brings precision.", "A paciência traz precisão.", "La paciencia trae precisión."));
        phrases.add(new Phrase("Feel the vibration through your fingertips.", "Sente a vibração através da ponta dos dedos.", "Siente la vibración a través de las puntas de tus dedos."));
        phrases.add(new Phrase("Drop your shoulders, raise your spirit.", "Baixa os ombros, eleva o teu espírito.", "Baja los hombros, eleva tu espíritu."));
        phrases.add(new Phrase("The bow is an extension of your arm.", "O arco é uma extensão do teu braço.", "El arco es una extensión de tu brazo."));
        phrases.add(new Phrase("Sing the phrase in your mind first.", "Canta a frase na tua mente primeiro.", "Canta la frase en tu mente primero."));
        phrases.add(new Phrase("Trust your muscle memory.", "Confia na tua memória muscular.", "Confía en tu memoria muscular."));
        phrases.add(new Phrase("Find the balance point of the bow.", "Encontra o ponto de equilíbrio do arco.", "Encuentra el punto de equilibrio del arco."));

        phrases.add(new Phrase("Breathe with the phrasing.", "Respira com o fraseado.", "Respira con el fraseo."));
        phrases.add(new Phrase("Soft hands produce a warm tone.", "Mãos suaves produzem um som quente.", "Manos suaves producen un sonido cálido."));
        phrases.add(new Phrase("Keep your joints unlocked.", "Mantém as tuas articulações destrancadas.", "Mantén tus articulaciones desbloqueadas."));
        phrases.add(new Phrase("Let gravity pull your arm down.", "Deixa a gravidade puxar o teu braço para baixo.", "Deja que la gravedad tire de tu brazo hacia abajo."));
        phrases.add(new Phrase("Embrace the natural resonance.", "Abraça a ressonância natural.", "Abraza la resonancia natural."));
        phrases.add(new Phrase("Your instrument is a mirror of your body.", "O teu instrumento é um espelho do teu corpo.", "Tu instrumento es un espejo de tu cuerpo."));
        phrases.add(new Phrase("Preparation is the key to execution.", "A preparação é a chave para a execução.", "La preparación es la clave de la ejecución."));
        phrases.add(new Phrase("Visualize the perfect sound.", "Visualiza o som perfeito.", "Visualiza el sonido perfecto."));
        phrases.add(new Phrase("Stay grounded in your stance.", "Mantém-te firme na tua postura.", "Mantente firme en tu postura."));
        phrases.add(new Phrase("Let the music guide your movements.", "Deixa a música guiar os teus movimentos.", "Deja que la música guíe tus movimientos."));

        phrases.add(new Phrase("Smooth bow changes create a seamless line.", "Mudanças de arco suaves criam uma linha contínua.", "Los cambios de arco suaves crean una línea continua."));
        phrases.add(new Phrase("Feel the friction, control the speed.", "Sente a fricção, controla a velocidade.", "Siente la fricción, controla la velocidad."));
        phrases.add(new Phrase("Relax your jaw and neck.", "Relaxa o maxilar e o pescoço.", "Relaja la mandíbula y el cuello."));
        phrases.add(new Phrase("Each string has its own voice.", "Cada corda tem a sua própria voz.", "Cada cuerda tiene su propia voz."));
        phrases.add(new Phrase("Play through the string, not just on it.", "Toca através da corda, não apenas sobre ela.", "Toca a través de la cuerda, no solo sobre ella."));
        phrases.add(new Phrase("Find the core of the note.", "Encontra o núcleo da nota.", "Encuentra el núcleo de la nota."));
        phrases.add(new Phrase("A relaxed grip is a strong grip.", "Uma pega relaxada é uma pega forte.", "Un agarre relajado es un agarre fuerte."));
        phrases.add(new Phrase("Listen to the overtones.", "Ouve os harmónicos.", "Escucha los armónicos."));
        phrases.add(new Phrase("Maintain a steady internal pulse.", "Mantém uma pulsação interna estável.", "Mantén un pulso interno estable."));
        phrases.add(new Phrase("Let your left hand fall into place.", "Deixa a tua mão esquerda cair no lugar.", "Deja que tu mano izquierda caiga en su lugar."));

        phrases.add(new Phrase("Articulate with intention.", "Articula com intenção.", "Articula con intención."));
        phrases.add(new Phrase("Keep the bow parallel to the bridge.", "Mantém o arco paralelo ao cavalete.", "Mantén el arco paralelo al puente."));
        phrases.add(new Phrase("Feel the shift before you move.", "Sente a mudança antes de te moveres.", "Siente el cambio antes de moverte."));
        phrases.add(new Phrase("Let the instrument ring freely.", "Deixa o instrumento soar livremente.", "Deja que el instrumento suene libremente."));
        phrases.add(new Phrase("Your breathing controls your rhythm.", "A tua respiração controla o teu ritmo.", "Tu respiración controla tu ritmo."));
        phrases.add(new Phrase("Find the natural weight of your hand.", "Encontra o peso natural da tua mão.", "Encuentra el peso natural de tu mano."));
        phrases.add(new Phrase("Let the melody sing.", "Deixa a melodia cantar.", "Deja que la melodía cante."));
        phrases.add(new Phrase("A calm mind creates a clear tone.", "Uma mente calma cria um som limpo.", "Una mente tranquila crea un sonido claro."));
        phrases.add(new Phrase("Connect the notes with invisible threads.", "Conecta as notas com fios invisíveis.", "Conecta las notas con hilos invisibles."));
        phrases.add(new Phrase("Enjoy the process of warming up.", "Aproveita o processo de aquecimento.", "Disfruta el proceso de calentamiento."));

        return phrases;
    }
}