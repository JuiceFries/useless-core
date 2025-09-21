package org.useless.uui;

import org.useless.Bag;

public class UselessUI extends Bag {

    private static final String Document = """
            介绍：该软件包是项目的核心包
            信息：一个不知搞啥的类懒得想用处
    
            """;


    /**
     * 获取文档
     * @return 文档
     */
    public static String getDocument() {
        return Document;
    }

}
