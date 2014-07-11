package org.jfugue.examples;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import org.jfugue.Player;
import org.jfugue.Pattern;

/**
 * This class generates a string of music based on a Lindenmayer System, which is a system of
 * grammar re-write rules.  Basically, you start with an initial string, or "axiom", like "G".
 * Then you go through the string, changing the string if you come across particular characters.
 * For example, we might say, "Convert all G's to AB".  Now the string would be "AB".  If we
 * do this again, and we have defitions for A and B ("Convert all A's to GA", "Convert all
 * B's to BB"), we'll get "GABB".  Do it again, and we get "ABGABBBB".
 *
 * <p>
 * Graphical representations of Lindenmayer Systems have been used to create realistic
 * plant forms.  This is really fascinating stuff, and I recommend you search on Google
 * for Lindenmayer Systems or L-Systems.
 * </p>
 *
 * <p>
 * In this class's L-Sys transformations, we use letters in angle brackets to represent
 * different rules.  The parser will ignore tokens with angle brackets, because it does
 * not recognize them as one of the commands it can parse.
 * </p>
 *
 * <p>
 * To compile: <code>javac -classpath <i>directory-and-filename-of-jfugue.jar</i> LSysMusic.java</code><br />
 * To execute: <code>java -classpath <i>directory-and-filename-of-jfugue.jar</i> org.jfugue.examples.LSysMusic<br />
 * (note: Depending on your version of Java, you may have to use "-cp" instead of "-classpath" when executing)<br />
 * </p>
 *
 * @author David Koelle
 * @version 2.0
 */
public class LSysMusic
{
    String axiom;
    LinkedList transforms;

    public LSysMusic()
    {
        this.axiom = "";
        this.transforms = new LinkedList();
    }

    public void setAxiom(String axiom)
    {
        this.axiom = axiom;
    }

    public void addTransform(String transformFrom, String transformTo)
    {
        Pair pair = new Pair(transformFrom, transformTo);
        transforms.add(pair);
    }

    public String generate(int iterations)
    {
        String newString = new String();
        String baseString = axiom;

        while (iterations > 0) {
            iterations--;
            newString = "";

            StringTokenizer strtok = new StringTokenizer(baseString," ");
            while (strtok.hasMoreTokens()) {
                String mod = strtok.nextToken();
                boolean matchFound = false;

                Iterator transformIter = transforms.iterator();
                while (transformIter.hasNext()) {
                    Pair p = (Pair)transformIter.next();
                    if (mod.equals(p.getFirst())) {
                        matchFound = true;
                        newString = newString + " " + p.getSecond();
                    }
                }

                if (!matchFound) {
                    newString = newString + " " + mod;
                }
            }

            baseString = newString;
        }
        return newString;
    }


    public static void main(String[] args)
    {
        LSysMusic musicGenerator = new LSysMusic();
        musicGenerator.setAxiom("T60 V0 I[Flute] <a> <b> <f> V1 I[Piano] <c> <d> <g>");
        musicGenerator.addTransform("<a>", "Cmaj4h <a> Emaj4h <b>");
        musicGenerator.addTransform("<b>", "Gmaj4h <e> Bbmaj4h <a>");
        musicGenerator.addTransform("<c>", "C5q G5q E5q G5q <d> <c>");
        musicGenerator.addTransform("<d>", "C5q G5q C6q G5q <e> <c>");
        musicGenerator.addTransform("<e>", "Eqi Cqi Eqi Cqi Eqi Cqi Eqi Cqi");
        musicGenerator.addTransform("<f>", "Cmaj4w");
        musicGenerator.addTransform("<g>", "G5q E5q C5h");
        String generatedMusic = musicGenerator.generate(3);

        Pattern pattern = new Pattern(generatedMusic);
        Player player = new Player();
        player.play(pattern);
        System.exit(0);
    }
}

class Pair
{
    private String a = new String();
    private String b = new String();

    public Pair(String a, String b)
    {
        this.a = a;
        this.b = b;
    }

    public String getFirst()
    {
        return this.a;
    }

    public String getSecond()
    {
        return this.b;
    }
}
