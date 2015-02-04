package com.company;

import models.abstracts.ProviderAbstract;
import models.logic.Core;
import models.providers.DropboxProvider;
import models.providers.GoogleDriveProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<ProviderAbstract> providers = new ArrayList<ProviderAbstract>();
        GoogleDriveProvider drive = new GoogleDriveProvider();
        providers.add(drive);
        DropboxProvider dropbox = new DropboxProvider();
        providers.add(dropbox);
        for (int i = 0; i < providers.size(); i++){
            ProviderAbstract provider = providers.get(i);
            System.out.println(provider.getLoginURL());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try{
                String code = br.readLine();
                System.out.println("token: " + code);
                System.out.println("setToken: " + provider.setToken(code));
                //System.out.println("uploadFile: " + provider.uploadFile("/home/raphabot/IdeaProjects/DistiCloudCLI/src/com/company/document.txt", "Teste 2.txt"));
            }catch (Exception e){}
        }
        //Core.encodeSplitUpload("/home/raphabot/IdeaProjects/DistiCloudCLI/src/com/company/document.txt", providers);
        System.out.println("uploadFile: " + providers.get(0).uploadFile("/home/raphabot/IdeaProjects/DistiCloudCLI/src/com/company/document.txt", "Teste 2.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String id = null;
        try {
            id = br.readLine();
            providers.get(0).downloadFile("dropbox-download",id.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        GoogleDriveProvider drive = new GoogleDriveProvider();
        System.out.println(drive.getLoginURL());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            String code = br.readLine();
            System.out.println("token: " + code);
            System.out.println("setToken: " + drive.setToken(code));
            System.out.println("uploadFile: " + drive.uploadFile(new File("/home/raphabot/IdeaProjects/DistiCloudCLI/src/com/company/document.txt"), "Teste 2"));
        }catch (Exception e){}
        DropboxProvider dropbox = new DropboxProvider();
        System.out.println(dropbox.getLoginURL());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            String code = br.readLine();
            System.out.println("token: " + code);
            System.out.println("setToken: " + dropbox.setToken(code));
            System.out.println("uploadFile: " + dropbox.uploadFile(new File("/home/raphabot/IdeaProjects/DistiCloudCLI/src/com/company/document.txt"), "Teste 2.txt"));
        }catch (Exception e){}
        */




        //providers.add(drive);

    }
}

/*
bd:
n contas (user, codigo, tipo)
n arquivos (nome)
cada arquivo tem n partes(posicao, id_cloud)
cada parte esta associada a uma conta
 */
