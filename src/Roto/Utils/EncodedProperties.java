package Roto.Utils;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

/**
 * The <code>Properties</code> class represents a persistent set of
 * properties. The <code>Properties</code> can be saved to a stream
 * or loaded from a stream. Each key and its corresponding value in
 * the property list is a string.
 * <p>
 * A property list can contain another property list as its
 * "defaults"; this second property list is searched if
 * the property key is not found in the original property list.
 * <p>
 * Because <code>Properties</code> inherits from <code>Hashtable</code>, the
 * <code>put</code> and <code>putAll</code> methods can be applied to a
 * <code>Properties</code> object.  Their use is strongly discouraged as they
 * allow the caller to insert entries whose keys or values are not
 * <code>Strings</code>.  The <code>setProperty</code> method should be used
 * instead.  If the <code>store</code> or <code>save</code> method is called
 * on a "compromised" <code>Properties</code> object that contains a
 * non-<code>String</code> key or value, the call will fail.
 * <p>
 * <a name="encoding"></a>
 * When saving properties to a stream or loading them from a stream, the
 * ISO 8859-1 character encoding can be used. For characters that cannot be directly
 * represented in this encoding,
 * <a href="http://java.sun.com/docs/books/jls/html/3.doc.html#100850">Unicode escapes</a>
 * are used; however, only a single 'u' character is allowed in an escape sequence.
 * The native2ascii tool can be used to convert property files to and from
 * other character encodings.
 * </p>
 * <p>
 * This Properties class is an extension of the default properties class an supports the
 * loading and saving from and into other encodings than ISO 8859-1.
 * </p>
 * @see <a href="../../../tooldocs/solaris/native2ascii.html">native2ascii tool for Solaris</a>
 * @see <a href="../../../tooldocs/win32/native2ascii.html">native2ascii tool for Windows</a>
 *
 * @version 1.64, 06/26/00
 * @since   JDK1.0
 */
public class EncodedProperties extends java.util.Properties {
    private static final String keyValueSeparators = "=: \t\r\n\f";

    private static final String strictKeyValueSeparators = "=:";

    private static final String specialSaveChars = "= \t\r\n\f";

    private static final String whiteSpaceChars = " \t\r\n\f";

    /** Creates a new instance of Properties */
    public EncodedProperties() {
    }

    @SuppressWarnings("unchecked")
    public synchronized Enumeration keys() {
        Enumeration keysEnum = super.keys();
        Vector keyList = new Vector();
        while (keysEnum.hasMoreElements()) {
            keyList.add(keysEnum.nextElement());
        }
        Collections.sort(keyList);
        return keyList.elements();
    }


    public synchronized void load(java.io.InputStream inStream,
                                  java.nio.charset.Charset encoding) throws java.io.IOException {
        if (encoding.equals(encoding.forName("8859_1"))) {
            super.load(inStream);
            return;
        }

        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(inStream, encoding));
        while (true) {
            // Get next line
            String line = in.readLine();
            if (line == null)
                return;

            if (line.length() > 0) {
                // Continue lines that end in slashes if they are not comments
                char firstChar = line.charAt(0);
                if ((firstChar != '#') && (firstChar != '!')) {
                    while (continueLine(line)) {
                        String nextLine = in.readLine();
                        if (nextLine == null)
                            nextLine = "";
                        String loppedLine = line.substring(0, line.length() - 1);
                        // Advance beyond whitespace on new line
                        int startIndex = 0;
                        for (startIndex = 0; startIndex < nextLine.length(); startIndex++)
                            if (whiteSpaceChars.indexOf(nextLine.charAt(startIndex)) == -1)
                                break;
                        nextLine = nextLine.substring(startIndex, nextLine.length());
                        line = new String(loppedLine + nextLine);
                    }

                    // Find start of key
                    int len = line.length();
                    int keyStart;
                    for (keyStart = 0; keyStart < len; keyStart++) {
                        if (whiteSpaceChars.indexOf(line.charAt(keyStart)) == -1)
                            break;
                    }

                    // Blank lines are ignored
                    if (keyStart == len)
                        continue;

                    // Find separation between key and value
                    int separatorIndex;
                    for (separatorIndex = keyStart; separatorIndex < len; separatorIndex++) {
                        char currentChar = line.charAt(separatorIndex);
                        if (currentChar == '\\')
                            separatorIndex++;
                        else if (keyValueSeparators.indexOf(currentChar) != -1)
                            break;
                    }

                    // Skip over whitespace after key if any
                    int valueIndex;
                    for (valueIndex = separatorIndex; valueIndex < len; valueIndex++)
                        if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
                            break;

                    // Skip over one non whitespace key value separators if any
                    if (valueIndex < len)
                        if (strictKeyValueSeparators.indexOf(line.charAt(valueIndex)) != -1)
                            valueIndex++;

                    // Skip over white space after other separators if any
                    while (valueIndex < len) {
                        if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
                            break;
                        valueIndex++;
                    }
                    String key = line.substring(keyStart, separatorIndex);
                    String value = (separatorIndex < len) ? line.substring(valueIndex, len) : "";

                    // Convert then store key and value
                    key = loadConvert(key);
                    value = loadConvert(value);
                    put(key, value);
                }
            }
        }
    }

    public synchronized void store(java.io.OutputStream out, java.nio.charset.Charset encoding,
                                   String header) throws java.io.IOException {
        if (encoding.equals(encoding.forName("8859_1"))) {
            super.store(out, header);
            return;
        }
        java.io.BufferedWriter awriter;
        awriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(out, encoding));
        if (header != null)
            writeln(awriter, "#" + header);
        writeln(awriter, "#" + new java.util.Date().toString());
        for (java.util.Enumeration e = keys(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            String val = (String) get(key);
            key = saveConvert(key, true);

            /* No need to escape embedded and trailing spaces for value, hence
	     * pass false to flag.
	     */
            val = saveConvert(val, false);
            writeln(awriter, key + "=" + val);
        }
        awriter.flush();
    }

    /*
     * changes special saved chars to their original forms
     */
    private String loadConvert(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 't')
                    aChar = '\t';
                else if (aChar == 'r')
                    aChar = '\r';
                else if (aChar == 'n')
                    aChar = '\n';
                else if (aChar == 'f')
                    aChar = '\f';
                else if (aChar == '\\')
                    aChar = '\\';
                else if (aChar == '\"')
                    aChar = '\"';
                else if (aChar == '\'')
                    aChar = '\'';
                else if (aChar == ' ')
                    aChar = ' ';
                else
                    throw new IllegalArgumentException("error in Encoding: '\\" + aChar + " not supported");
                outBuffer.append(aChar);
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
    /*
     * writes out any of the characters in specialSaveChars
     * with a preceding slash
     */
    private String saveConvert(String theString, boolean escapeSpace) {
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len * 2);

        for (int x = 0; x < len; x++) {
            char aChar = theString.charAt(x);
            switch (aChar) {
            case ' ':
                if (x == 0 || escapeSpace)
                    outBuffer.append('\\');

                outBuffer.append(' ');
                break;
            case '\\':
                outBuffer.append('\\');
                outBuffer.append('\\');
                break;
            case '\t':
                outBuffer.append('\\');
                outBuffer.append('t');
                break;
            case '\n':
                outBuffer.append('\\');
                outBuffer.append('n');
                break;
            case '\r':
                outBuffer.append('\\');
                outBuffer.append('r');
                break;
            case '\f':
                outBuffer.append('\\');
                outBuffer.append('f');
                break;
            default:
                //                    if ((aChar < 0x0020) || (aChar > 0x007e)) {
                //                        outBuffer.append(aChar);
                //                    } else {
                if (specialSaveChars.indexOf(aChar) != -1)
                    outBuffer.append('\\');
                outBuffer.append(aChar);
                //                    }
            }
        }
        return outBuffer.toString();
    }
    /*
     * Returns true if the given line is a line that must
     * be appended to the next line
     */
    private boolean continueLine(String line) {
        int slashCount = 0;
        int index = line.length() - 1;
        while ((index >= 0) && (line.charAt(index--) == '\\'))
            slashCount++;
        return (slashCount % 2 == 1);
    }

    private static void writeln(java.io.BufferedWriter bw, String s) throws java.io.IOException {
        bw.write(s);
        bw.newLine();
    }
}

