package com.company;

import models.abstracts.ProviderAbstract;
import models.providers.GoogleDriveProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        //ArrayList<ProviderAbstract> providers = new ArrayList<ProviderAbstract>();
        GoogleDriveProvider drive = new GoogleDriveProvider();
        System.out.println(drive.getLoginURL());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            String code = br.readLine();
            System.out.println("token: " + code);
            System.out.println("setToken: " + drive.setToken(code));
            System.out.println("uploadFile: " + drive.uploadFile(new File("/home/raphabot/IdeaProjects/DistiCloudCLI/src/com/company/document.txt"), "Teste 2"));
        }catch (Exception e){}



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
