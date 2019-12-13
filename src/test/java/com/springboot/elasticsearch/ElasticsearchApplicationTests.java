package com.springboot.elasticsearch;

import com.springboot.elasticsearch.bean.Article;
import com.springboot.elasticsearch.bean.Book;
import com.springboot.elasticsearch.repository.BookRepository;
import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import java.io.File;
//import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchApplicationTests {
    @Autowired
    JestClient jestClient;

    @Autowired
    BookRepository bookRepository;
    @Autowired
    JavaMailSenderImpl javaMailSender;

    //这个是直接创建了index和带有文档内容
    @Test
    public void contextLoads() {
        Article article = new Article();
        article.setId(1);
        article.setTitle("好消息");
        article.setAuthor("zhangsan");
        article.setContent("Hello World");
        Index index = new Index.Builder(article).index("ballshit").type("dickman").build();
        try {
            //执行
            jestClient.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //查询符合hellodoc内容
    @Test
    public void search() {
        String json = "{\n" +
                "    \"query\" : {\n" +
                "        \"match\" : {\n" +
                "            \"content\" : \"hello\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        Search search = new Search.Builder(json).addIndex("ballshit").addType("dickman").build();
        try {
            SearchResult result = jestClient.execute(search);
            System.out.println(result.getJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除文档根据文档Id
    @Test
    public void delete() {

        try {
            jestClient.execute(new Delete.Builder("1")
                    .index("ballshit")
                    .type("dickman")
                    .build());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //使用spring-data 整合es
    @Test
    public void test01() {
//		Book book = new Book();
//		book.setId(1);
//		book.setBookName("西游记");
//		book.setAuthor("吴承恩");
//		bookRepository.index(book);
        for (Book book : bookRepository.findByBookNameLike("游")) {
            System.out.println(book);
        }
    }

    @Test
    public void mailtest01() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("通知，今天下午不上班");
        message.setText("今天下午去办理签证，到天河区软件路13号2楼政务中心办理");
        message.setTo("18875143382@163.com");
        message.setFrom("541534048@qq.com");
        javaMailSender.send(message);
    }

    @Test
    public void mailtest02() throws  Exception{
        //1、创建一个复杂的消息邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        //邮件设置
        helper.setSubject("通知-今晚开会");
        helper.setText("<b style='color:red'>今天 7:30 开会</b>",true);

        helper.setTo("18875143382@163.com");
        helper.setFrom("541534048@qq.com");

        //上传文件
        helper.addAttachment("1.jpg",new File("E:\\学习\\素材文件\\1.jpg"));
        helper.addAttachment("2.jpg",new File("E:\\学习\\素材文件\\2.jpg"));

        javaMailSender.send(mimeMessage);

    }
}
