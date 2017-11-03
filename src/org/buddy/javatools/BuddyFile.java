/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buddy.javatools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

/**
 * Buddy IO class
 * 
 * @author xinqiyang
 */
public class BuddyFile {

    /**
     * @param fileName
     * @param content
     * @param append
     * @throws IOException void
     */
    public static void write(String fileName, String content, boolean append) throws IOException {
        FileWriter fw = null;
        try {
            File file = new File(fileName);
            fw = new FileWriter(file, append);
            fw.write(content);

        } catch (IOException e) {
            Utils.getLog().error("write file[" + fileName + "] exception", e);
        } finally {
            IOUtils.closeQuietly(fw);
        }
    }

    /**
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException String
     */
    public static String get(String fileName) throws FileNotFoundException, IOException {
        String s = "";
        StringBuilder content = new StringBuilder();
        File file = new File(fileName);
        if (file.exists()) {
            BufferedReader br = null;
            try {
                FileReader fileread = new FileReader(file);
                br = new BufferedReader(fileread);

                while ((s = br.readLine()) != null) {
                    content.append(s);
                }
            } catch (IOException e) {
                Utils.getLog().error("read file[" + fileName + "] exception", e);
            } finally {
                IOUtils.closeQuietly(br);
            }
        }
        return content.toString();
    }

}
