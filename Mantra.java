package io.temporal.learning;

import java.util.Random;

public class Mantra {
    private static final String[] MANTRAS = {
        "Today’s choices, clarity, intent, and awareness create my tomorrow.",
        "Today is a beautiful day of opportunity. I am exactly where I need to be. I open myself to the universe and trust in the unfolding of my life.",
        "Today, I welcome my chance to feel energized, healthy, and powerful.",
        "As I move through my today, I gather positivity and hope.",
        "The sun comes up so I can feel strong.",
        "I will greet my today with confidence and poise.",
        "My path today is clear and free from obstacles. I will welcome each moment as a gift.",
        "I accept myself exactly as I am. I am worthy of love and should be cherished. I open my heart both to giving and receiving kindness.",
        "I breathe. Air fills my lungs and my body fills with energy. The breath brings me joy, brings me calm, brings me excitement for all today's opportunities.",
        "I am thankful for this new day of opportunity.",
        "The air I breathe, and the ground I walk on, are all gifts from the Universe, I express my gratitude for the same.",
        "I am grateful for this fresh re-born moment that brings an entire day ahead of me.",
        "The Universe will bring me beauty and meaning throughout my today.",
        "Joy is mine at any moment today. I can reach out and touch it.",
        "My day today will be filled with beauty and wonder.",
        "I have everything I need to make today a wonderful day.",
        "You are doing great. Keep going. You can do this. You are exactly where you need to be.",
        "I trust myself to put my mask on first. I promise to be kind to myself today. I will do at least one thing today solely for myself.",
        "I am grateful for the gift of the world and how the world touches me, regardless of the season.",
        "I will own my truth today. I use my senses and my choices to build my actions.",
        "Control is the enemy of anxiety. Control is the enemy of stress.",
        "I am strong, gentle, and full of goodness.",
        "I can change every moment by choosing to name it. The name gives me power.",
        "I am open to what the Universe will offer me today.",
        "I am myself. And that is enough.",
        "I feel the warmth of a sun filling me with energy and power.",
        "Today, I will stand tall and strong because I have that power over my life.",
        "I face today with honesty and integrity.",
        "I will let the flow of life guide me down the river as I steer the boat of my choices.",
        "Challenges are my chance to grow today.",
        "Even the smallest task is a gift of accomplishment. I can tackle any problem by breaking it into parts.",
        "I am grateful for this life, for this home, for the changes in my life.",
        "I am surrounded by goodness. It's up to me to find it today.",
        "Today I will adjust myself to meet my challenges and overcome them.",
        "I am surrounded by elements of abundance and richness. I will open my eyes and thank them for participating in my life.",
        "Today I recognize my satisfaction when I look back at what I've accomplished. I will greet my moments of success with the praise I deserve.",
        "My life is meaningful and important; I am a valuable contributor to the greater good.",
        "I choose to be kind to myself and love myself unconditionally.",
        "Today I will remember to see my problems as challenges that evolve me and make me grow.",
        "Happiness is within me, waiting for me. I base my happiness on my own accomplishments and the blessings I've been given.",
        "As I focus on compassion, I naturally relate to others with love and understanding.",
        "I am not afraid of what could go wrong. I am excited about what could go right.",
        "Where I think I am lost, I am ready to feel found.",
        "As I forgive myself, it becomes easier to forgive others.",
        "I have the freedom and power to create the life I desire.",
        "Day by day and thought by thought, I am creating my ideal life!",
        "I am grateful for everything, the good and the bad because it made me.",
        "I am ready to encounter opportunities I never thought were possible.",
        "Today, I choose to think positively and create an amazing and successful life for myself.",
        "Each day, I am bold, and I walk on my path with courage!",
        "I am the architect of my life. I build its foundation and choose its design. I decorate my life with hope, healing, and gratitude.",
        "Self–acceptance, self–love, and self–care are the habits that I choose to cultivate. I am practicing loving my life.",
        "I trust myself and turn inward to seek my highest truth.",
        "I am worthy of great love and I deserve to be loved fully and completely.",
        "Each and every day I learn new lessons, expand my awareness, and develop my abilities.",
        "Today I am planting the seeds of growth in my life. I commit to watering them every day.",
        "It does not matter what other people say or do. What matters is how I choose to react and what I choose to believe about myself.",
        "I return to the basics of life: forgiveness, courage, gratitude, love, and humor.",
        "With every breath I take, I am bringing more and more gratitude into my life.",
        "Today, I will concentrate on taking one step forward, however small.",
        "My life is meaningful and important; I am a valuable contributor to the greater good.",
        "I am worthy because I say so. My worth is in my hands.",
        "Today, my abundance and joy will spill over to the people around me.",
        "Today I wake up with excitement and I begin my day with purpose.",
        "I recognize my gratitude for the many things in my life that bring me joy and comfort.",
        "I trust that what's meant for me is already mine, regardless of how and when it is mine.",
        "I am worthy of beautiful endings and exciting beginnings.",
        "I allow my voice to be heard, my thoughts to be expressed and my vision to be seen.",
        "I am open to things working out for me. I am open to receiving abundance. I am open to connecting with my highest self.",
        "I am eternally grateful for all of the blessings I have in my life.",
        "I bring calmness into my heart so I can recharge for my day.",
        "I breathe in the wonder and joy of a new day. I relax my body and prepare for each gift I will discover.",
        "Today I will practice self-compassion and treat myself with kindness.",
        "Today I will allow myself to fully experience my emotions and thoughts.",
        "Today is a day of acceptance, whatever life delivers to me.",
        "I pledge to myself to be authentically me today and everyday after.",
        "Today I will forgive myself and allow myself to find my small happinesses. I will smile, accept, and be one with my world.",
        "I deserve to treat myself with kindness. Today, I will work towards stilling my inner voice of criticism and embrace myself for who I am. I will encourage myself and celebrate my imperfect self. I am a gift to the world.",
        "Watch your thoughts, they become your words; watch your words, they become your actions; watch your actions, they become your habits; watch your habits, they become your character; watch your character, it becomes your destiny.",
        "Today I will find moments to stop and introspect so I can recognize the good that surrounds me in my life. I promise to acknowledge and thank these wonderful things.",
        "Today I will remember that I already am worthy and have left a trail of accomplishment in my life path."
    };

    private static final Random random = new Random(System.nanoTime());

    /**
     * Returns a random mantra from the array of mantras.
     *
     * @return a random mantra
     */
    public static String getRandomMantra() {
        int index = random.nextInt(MANTRAS.length);
        return MANTRAS[index];
    }
}
