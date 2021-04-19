package com.affise.tests.Run;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.testng.annotations.Test;

import java.io.*;

public class ExecCommand {

    @Test()
    public void RunSSHConnect() throws JSchException, InterruptedException {
        Session session = null;
        ChannelExec channel = null;

        try {
            session = new JSch().getSession("root", "10.201.0.173");
//            session.setPassword("vlad12-8");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("ls -l");
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.connect();

            while (channel.isConnected()) {
                Thread.sleep(100);
            }

            String responseString = new String(responseStream.toByteArray());
            System.out.println(responseString);
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

    @Test()
    public void RuntimeExec() {
        try {
            Process pr = Runtime.getRuntime().exec("ls -l", new String[]{"var1=value1"}, new File("/Users/shmakvlad/Documents"));

            // Process pr = Runtime.getRuntime().exec("ls -l /Users/shmakvlad/Downloads");
            // Process pr = Runtime.getRuntime().exec("ls -l", null, new File("/Users/shmakvlad/Documents"));

            // Run a shell script
            // Process pr = Runtime.getRuntime().exec("/Users/shmakvlad/Downloads/hello.sh");

            try {
                pr.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = "";
            while ((line = buf.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test()
    public void ProcessBuilder() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "ls -l /Users/shmakvlad/Downloads");

        // processBuilder.command("bash", "-c", "ls -l").directory(new File("/Users/shmakvlad/Downloads"));

        // Run a shell script
        // processBuilder.command("/Users/shmakvlad/Download/hello.sh");

        try {
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
                System.out.println(output);
                System.exit(0);
            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
