/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import threadCrawler.XemPhimSoThread;

/**
 *
 * @author AnhTHT
 */
public class MyContextServletListener {
    private static String realPath="";

    private static XemPhimSoThread xemPhimSoThread= new XemPhimSoThread(null);

    public static void main(String[] args) {
        //statrt thread
        xemPhimSoThread.start();
    }
}
