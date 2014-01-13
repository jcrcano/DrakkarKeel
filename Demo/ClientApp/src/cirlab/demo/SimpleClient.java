/*
 * @(#)SimpleClient.java   2.0.0   26/11/2013
 *
 * CIRLab - Collaborative Information Retrieval Laboratory
 * Webpage: unascribed
 * Contact: cirlab-dev@googlegroups.com
 * University of Granada - Department of Computing Science and Artificial
 * Intelligence
 * http://www.ugr.es/
 * University of the Informatics Sciences - Holguin Software Development Center
 * http://www.uci.cu/
 *
 * The contents of this file are subject under the terms described in the
 * CIRLAB_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License.
 *
 * Copyright (C) 2008-2013 CIRLab. All Rights Reserved.
 *
 * Contributor(s):
 *   RODRIGUEZ-AVILA, HUMBERTO <hrodrigueza{a.}facinf.uho.edu.cu>
 *   LEYVA-AMADOR, YOVIER <yovi{a.}hol.inv.cu>
 *   RODRIGUEZ-CANO, JULIO CESAR <jcrcano{a.}uci.cu>
 */
package cirlab.demo;

import drakkar.prow.facade.desktop.event.ProwAdapter;
import drakkar.prow.facade.desktop.event.SearchEvent;
import drakkar.prow.facade.desktop.event.SearchAdapter;
import drakkar.prow.facade.desktop.event.SeekerEvent;
import drakkar.prow.RequestSearchFactory;
import drakkar.prow.DrakkarProw;
import drakkar.oar.Request;
import drakkar.oar.DocumentMetaData;
import drakkar.oar.ResultSetMetaData;
import drakkar.oar.Response;
import java.io.*;
import drakkar.oar.slice.error.RequestException;
import drakkar.oar.util.Invocation;
import drakkar.oar.util.OutputMonitor;
import java.util.ArrayList;
import static drakkar.oar.util.KeyTransaction.*;
import static drakkar.oar.util.KeySession.*;
import java.net.MalformedURLException;

public class SimpleClient {

    static int status = -1;
    static boolean notified = false;
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static DrakkarProw client;
    static ArrayList<DocumentMetaData> results;

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public static void main(String[] args) throws IOException, RequestException {
        while (true) {
            System.out.println("Set the server IP or DNS name:");
            String host = reader.readLine();
            String[] param = {"-sh", host};
            client = new DrakkarProw(param);

            ProwAdapter clientAdapter = new ProwAdapter() {
                @Override
                public void notifyRequestConnection(SeekerEvent evt) {
                    Response response = evt.getResponse();
                    int reply = Integer.valueOf(response.get(REPLY).toString());
                    switch (reply) {
                        case CONNECTION_FAILED:
                            OutputMonitor.printLine("Connection failed.", OutputMonitor.ERROR_MESSAGE);
                            System.out.println("Finalizing...");
                            System.exit(1);
                            break;
                        case CONNECTION_SUCCESSFUL:
                            OutputMonitor.printLine("Connection successful.", OutputMonitor.INFORMATION_MESSAGE);
                            showMenu();
                            break;
                    }
                }
            };

            SearchAdapter searchAdapter = new SearchAdapter() {
                @Override
                public void notifySearchResults(SearchEvent se) {
                    Response response = se.getResponse();
                    ResultSetMetaData list = (ResultSetMetaData) response.get(SEARCH_RESULTS);
                    results = (ArrayList<DocumentMetaData>) list.getAllResultList();
                    if (status == 0) {
                        viewResult();
                    }
                }
            };

            client.getClientListenerManager().addClientListener(clientAdapter);
            client.getClientListenerManager().addSearchListener(searchAdapter);
            try {
                System.out.println("Please wait...");
                client.loginSearchCollabSession();
                break;
            } catch (Exception e) {
                System.out.println("Server not found.");
            }
        }
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch"})
    public static void showMenu() {
        status = -1;
        String query = "";
        while (true) {
            int option = 0;
            System.out.println("***** Menu ************");
            System.out.println("* -(1) Search         *");
            System.out.println("* -(2) Exit           *");
            System.out.println("***********************");
            System.out.print("Enter your choice:");
            while (true) {
                try {
                    option = Integer.parseInt(reader.readLine());
                    break;
                } catch (Exception ex) {
                    System.out.println("Choose one correct number!");
                }
            }
            if (option == 2) {
                logout();
            }
            System.out.print("Enter your query: ");
            while (true) {
                try {
                    query = reader.readLine();
                    break;
                } catch (Exception ex) {
                    System.out.println("Error, write query it again!");
                }
            }
            break;
        }
        status = 0;
        System.out.println("Please wait...");
        Request request = RequestSearchFactory.create(query, RequestSearchFactory.META_SEARCH_AND_SPLIT);
        client.send(request, Invocation.SYNCHRONOUS_METHOD_INVOCATION);
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch"})
    private static void viewResult() {
        status = 1;
        if (results != null) {
            a:
            while (true) {
                int option = 0;
                int size = results.size();
                System.out.println("***** Menu ************");
                System.out.println("*** View Results ******");
                for (int i = 0; i < size; i++) {
                    System.out.println("* -(" + (i + 1) + ") " + results.get(i).getName());
                    System.out.println("*       URI: " + processPath(results.get(i).getPath()));
                }
                System.out.println("* -(" + (size + 1) + ") Back Menu");
                System.out.println("***********************");
                System.out.print("Enter your choice:");
                while (true) {
                    try {
                        option = Integer.parseInt(reader.readLine());
                        if (size != results.size()) {
                            System.out.println("Updating list...");
                            continue a;
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Choose one correct number!");
                    }
                }
                if (option == (results.size() + 1)) {
                    status = 0;
                    break;
                }
                if (option > 0 && option <= results.size()) {
                    getFile(results.get(option - 1).getPath());
                } else {
                    System.out.println("Choose one number correct!");
                }
            }
        } else {
            System.out.println("Not result found.");
        }
        showMenu();
    }

    private static void logout() {
        System.out.println("Please wait...");
        client.logout();
        System.out.println("User logout successfully");
        System.out.println("finalized.........");
        System.exit(1);
    }

    private static String processPath(String path) {
        File f = new File(path);
        String newPath = null;
        try {
            newPath = f.toURI().toURL().toString().substring(9);
        } catch (MalformedURLException ex) {
            OutputMonitor.printStream("", ex);
        }
        return client.getCnxServer().getServerHost() + "://" + newPath;
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch"})
    private static void getFile(final String url) {
        try {
            client.downloadAndOpenFile(url);
        } catch (Exception e) {
            System.out.println("Failed when opening the file");
        }
    }
}