package com.druson.cycle.utils;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by Zoson on 16/5/7.
 */
public class FileUtils {

    public static void deleteDir(String path){
        Stack<File> stack = new Stack();
        Queue<File> queue = new LinkedList();
        File root = new File(path);
        queue.offer(root);
        while(!queue.isEmpty()){
            File file = queue.poll();
            stack.push(file);
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for(int i=0;i<files.length;i++){
                    queue.offer(files[i]);
                }
            }
        }
        while(!stack.isEmpty()){
            File f = stack.pop();
            f.delete();
        }
    }
}
