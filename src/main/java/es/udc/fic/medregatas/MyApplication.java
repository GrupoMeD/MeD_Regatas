/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.fic.medregatas;

import es.udc.fic.medregatas.view.MainAppjFrame;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author iago
 */
public class MyApplication {

    public static void main(String[] args) throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring-config.xml");
        final MainAppjFrame mainAppjFrame = new MainAppjFrame(context);

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainAppjFrame.setLocationRelativeTo(null);
                mainAppjFrame.setVisible(true);
            }
        });
    }

}
