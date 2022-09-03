package com.ignatt.filedivider;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Controller {
    private double separator;
    private int separationType;

    public void separate(int separationType, double separator, List<String> guidsArray){
        this.separator = separator;
        this.separationType = separationType;

        File dataFolder = new File("data");
        dataFolder.mkdir();

            if (separationType == 1){
                double countFiles = (guidsArray.size() - 1) / separator;
                int numberFile = 0;
                int startLine = 0;
                int endLine = (int)Math.round(separator);
                String dateCurrent= new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

                File log = new File(dataFolder.getPath() + "\\" + "log.txt");
                    try {
                        log.createNewFile();
                        PrintWriter pwLog = new PrintWriter(log);
                        pwLog.println("in topic.kafka.name need send a messages");

                        while (countFiles > numberFile){
                            String korrUUID = java.util.UUID.randomUUID().toString();
                            File divideFiles = new File(dataFolder.getPath() + "\\" + "file_" + startLine + "-" + endLine + "_" + dateCurrent + ".xml");
                            divideFiles.createNewFile();

                            PrintWriter pw = new PrintWriter(divideFiles);
                            pw.println("<ns7:data xmlns:basis=\"urn://site-ru/types/prsn-basis/1.0.2\" xmlns:site=\"urn://site-ru/types/basic/1.0.8\" xmlns:fact=\"urn://site-ru/types/assignment-fact/1.0.8\" xmlns:ns7=\"urn://site-ru/msg/10.06.S/1.0.8\" xmlns:pac=\"urn://site-ru/types/package-RAF/1.0.9\" xmlns:prsn=\"urn://site-ru/types/prsn-info/1.0.4\" xmlns:smev=\"urn://x-artefacts-smev-gov-ru/supplementary/commons/1.0.1\">\n" +
                                    "\t<pac:package>\n" + "\t\t<pac:packageId>" + korrUUID + "</pac:packageId>\n" + "\t\t<pac:elements>");
                            pwLog.println(divideFiles.getName() + " = kafka_correlationId: " + korrUUID);
                            getBodyXML(startLine, endLine, guidsArray, pw);

                            startLine = startLine + (int)Math.round(separator);
                            endLine = endLine + (int)Math.round(separator);
                            numberFile++;
                        }
                        pwLog.println("kafka_messageType PACKAGE_CHANGE_REGISTRY_ASSIGNMENT_FACT\n" + "kafka_correlationId uuids \n" + "kafka_participantCode 0001.000000\n");
                        pwLog.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            } else if (separationType == 2) {

                double countRZ = (guidsArray.size() - 1) / separator;
                int numberRZ = 0;
                int startLine = 0;
                int endLine = (int)Math.round(separator);
                String dateCurrent = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                String korrUUID = java.util.UUID.randomUUID().toString();

                while (countRZ > numberRZ){

                    File folders = new File(dataFolder + "\\" + "РЗ_" + numberRZ + "_" + startLine + "-" + endLine);
                    File divideGuids = new File(folders.getPath() + "\\" + "current_guids_" + startLine + "-" + endLine + "_" + dateCurrent + ".xml");
                    folders.mkdir();
                    try {
                        divideGuids.createNewFile();
                        File txtForRZ = new File(folders.getPath() + "\\" + "РЗ_" + numberRZ + ".txt");
                        txtForRZ.createNewFile();

                        PrintWriter pw = new PrintWriter(divideGuids);
                        pw.println("<ns7:data xmlns:basis=\"urn://site-ru/types/prsn-basis/1.0.2\" xmlns:site=\"urn://site-ru/types/basic/1.0.8\" xmlns:fact=\"urn://site-ru/types/assignment-fact/1.0.8\" xmlns:ns7=\"urn://site-ru/msg/10.06.S/1.0.8\" xmlns:pac=\"urn://site-ru/types/package-RAF/1.0.9\" xmlns:prsn=\"urn://site-ru/types/prsn-info/1.0.4\" xmlns:smev=\"urn://x-artefacts-smev-gov-ru/supplementary/commons/1.0.1\">\n" +
                        "\t<pac:package>\n" + "\t\t<pac:packageId>" + korrUUID + "</pac:packageId>\n" + "\t\t<pac:elements>");
                        getBodyXML(startLine,endLine,guidsArray,pw);

                        PrintWriter pwTxtForRz = new PrintWriter(txtForRZ);
                        pwTxtForRz.println("generate body xml\n" + "\n" + "Go this body in topic topic.site.request\n" + "\n" + "body: "+ divideGuids.getName() +" \n" +
                        "and headers:\n" + "kafka_messageType PACKAGE_CHANGE_REGISTRY_ASSIGNMENT_FACT\n" + "kafka_correlationId " + korrUUID + "\n" +
                        "kafka_participantCode 0001.000000\n" + "\n" + "Key no need\n" + "\n" +
                        "after push int topic need a partition and offset kafka " + korrUUID + " - this uuid in body xml.");
                        pwTxtForRz.close();
                        startLine = startLine + (int)Math.round(separator);
                        endLine = endLine + (int)Math.round(separator);
                        numberRZ++;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    public void getBodyXML(int startLine, int endLine, List<String> guidsArray, PrintWriter pw){
        for(int i = startLine; i < endLine; i++) {
            if (i == guidsArray.size()) {
                break;
            }
            pw.println("<pac:errorsInvalidation>\n" + "\t\t\t\t<pac:uuid>" + java.util.UUID.randomUUID() + "</pac:uuid>\n" +
                    "\t\t\t\t<pac:assignmentFactUuid>" + guidsArray.get(i) + "</pac:assignmentFactUuid>\n" +
                    "\t\t\t\t<pac:providerCode>0001.000000</pac:providerCode>\n" + "\t\t\t</pac:errorsInvalidation>" );
        }
        pw.println("\t\t</pac:elements>\n" + "\t</pac:package>\n" + "</ns7:data>");
        pw.close();
    }

}
