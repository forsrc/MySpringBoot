package com.forsrc.utils;

import java.io.*;
import java.text.MessageFormat;

/**
 * The type Cmd utils.
 */
public class CmdUtils {

    /**
     * Cmd int.
     *
     * @param cmd the cmd
     * @return the int
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    public static int cmd(String[] cmd) throws IOException,
            InterruptedException {
        if (cmd == null || cmd.length == 0) {
            return -100;
        }
        LogUtils.LOGGER.debug(" cmd [START] ...");
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new IOException(e);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                process.getInputStream()));

        String str = null;
        try {
            while ((str = br.readLine()) != null) {
                System.out.println(str);
            }

        } catch (IOException e) {
            throw new IOException(e);
        }

        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }

        BufferedReader brerr = new BufferedReader(new InputStreamReader(
                process.getErrorStream()));

        String strerr = null;
        try {
            while ((strerr = brerr.readLine()) != null) {
                System.out.println(strerr);
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
        if (brerr != null) {
            try {
                brerr.close();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
        int rc = -1000;
        try {
            rc = process.waitFor();
        } catch (InterruptedException e) {
            throw new InterruptedException(e.toString());
        }
        process.destroy();
        LogUtils.LOGGER.debug(MessageFormat.format(" cmd [END]   ReturnValue: {0}", rc));
        return rc;

    }

    /**
     * Cmd int.
     *
     * @param cmd  the cmd
     * @param cmds the cmds
     * @return the int
     * @throws InterruptedException the interrupted exception
     * @throws IOException          the io exception
     */
    public static int cmd(String[] cmd, String[] cmds)
            throws InterruptedException, IOException {
        if (cmd == null || cmd.length == 0) {
            return -100;
        }
        LogUtils.LOGGER.debug(" cmd [START] ...");
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new IOException(e);
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                process.getOutputStream()));
        new CmdWriterThread(bw, cmds).start();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                process.getInputStream()));

        new CmdReaderThread(br).start();

        BufferedReader brerr = new BufferedReader(new InputStreamReader(
                process.getErrorStream()));
        new CmdReaderThread(brerr).start();

        int rc = -1000;
        try {
            rc = process.waitFor();
        } catch (InterruptedException e) {
            throw new InterruptedException(e.toString());
        }
        if (bw != null) {
            try {
                bw.close();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
        if (brerr != null) {
            try {
                brerr.close();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
        process.destroy();
        LogUtils.LOGGER.debug(MessageFormat.format(" cmd [END]   ReturnValue: {0}", rc));
        return rc;
    }

    /**
     * Cmd int.
     *
     * @param cmd the cmd
     * @param is  the is
     * @return the int
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    public static int cmd(String[] cmd, InputStream is) throws IOException,
            InterruptedException {
        if (cmd == null || cmd.length == 0) {
            return -100;
        }
        LogUtils.LOGGER.debug(" cmd [START] ...");
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new IOException(e);
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                process.getOutputStream()));
        new CmdWriterThread(bw, is).start();

        BufferedReader br = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        new CmdReaderThread(br).start();

        BufferedReader brerr = new BufferedReader(new InputStreamReader(
                process.getErrorStream()));
        new CmdReaderThread(brerr).start();

        int rc = -1000;
        try {
            rc = process.waitFor();
        } catch (InterruptedException e) {
            throw new InterruptedException(e.toString());
        }

        if (bw != null) {
            try {
                bw.close();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
        if (brerr != null) {
            try {
                brerr.close();
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
        process.destroy();
        LogUtils.LOGGER.debug(MessageFormat.format("cmd [END]   ReturnValue: {0}", rc));
        return rc;
    }

    /**
     * Print mark.
     *
     * @param mark the mark
     * @return void
     * @throws
     * @Title: printMark
     * @Description:
     */
    public static void printMark(String mark) {
        if (mark == null || "".equals(mark)) {
            return;
        }
        System.out.print(mark);
        for (int i = 0; i < (mark).length() + 1 + 1; i++) {
            System.out.print("\b");
        }
        System.out.print("\r");
        // try {
        // Thread.sleep(1);
        // } catch (InterruptedException e) {
        // }
    }

    /**
     * Print mark.
     *
     * @param mark   the mark
     * @param millis the millis
     */
    public static void printMark(String mark, long millis) {
        if (mark == null || "".equals(mark)) {
            return;
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            LogUtils.LOGGER.debug(e.getMessage());
        }
        System.out.print(mark);
        for (int i = 0; i < (mark).length() + 1 + 1; i++) {
            System.out.print("\b");
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LogUtils.LOGGER.debug(e.getMessage());
        }
        System.out.print("\r");
    }

    /**
     * The type Cmd writer thread.
     */
    static class CmdWriterThread extends Thread {
        /**
         * The Bw.
         */
        BufferedWriter bw;
        /**
         * The Br.
         */
        BufferedReader br;
        /**
         * The Cmds.
         */
        String[] cmds;

        /**
         * Instantiates a new Cmd writer thread.
         *
         * @param writer the writer
         * @param in     the in
         */
        CmdWriterThread(BufferedWriter writer, InputStream in) {
            this.bw = writer;
            this.br = new BufferedReader(new InputStreamReader(in));
            this.setDaemon(true);
        }

        /**
         * Instantiates a new Cmd writer thread.
         *
         * @param writer the writer
         * @param cmds   the cmds
         */
        CmdWriterThread(BufferedWriter writer, String[] cmds) {
            this.bw = writer;
            this.cmds = cmds;
            this.setDaemon(true);
        }

        @Override
        public void run() {
            try {

                if (this.cmds != null && this.cmds.length > 0) {
                    for (String cmd : this.cmds) {
                        this.bw.write(cmd);
                        this.bw.flush();
                    }
                    return;
                }
                if (this.br == null) {
                    return;
                }

                String cmd = null;
                while ((cmd = this.br.readLine()) != null) {
                    String lf = cmd.length() >= 2 ? cmd.substring(
                            cmd.length() - 2).equals("\\n") ? "\n" : "" : "";
                    String crlf = cmd.length() >= 4 ? cmd.substring(
                            cmd.length() - 4).equals("\\r\\n") ? "\r\n" : ""
                            : "";
                    cmd = crlf.equals("") ? lf.equals("") ? cmd : cmd
                            .substring(0, cmd.length() - 2) + lf : cmd
                            .substring(0, cmd.length() - 4) + crlf;

                    this.bw.write(cmd);
                    this.bw.flush();
                }
            } catch (IOException e) {
                LogUtils.LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * The type Cmd reader thread.
     */
    static class CmdReaderThread extends Thread {
        /**
         * The Br.
         */
        BufferedReader br = null;

        /**
         * Instantiates a new Cmd reader thread.
         *
         * @param br the br
         */
        CmdReaderThread(BufferedReader br) {
            this.br = br;
            this.setDaemon(true);
        }

        @Override
        public void run() {
            try {
                String s = null;
                while ((s = this.br.readLine()) != null) {
                    System.out.println(s);
                }
            } catch (IOException e) {
                LogUtils.LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
