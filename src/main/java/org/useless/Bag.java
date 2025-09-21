package org.useless;

public class Bag {

    public Bag() {

    }

    private static final String Document = """
            介绍：这只是个没有任何用处的类你不用看了它没有任何用处
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
