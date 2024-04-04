package com.mygdx.game.gpt;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.mygdx.game.utils.JavaSourceFromString;
import com.mygdx.game.core.MyGdx;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.utils.TikTokensUtil;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

public class GPTtext {

    public static class СhangeСolorText {
        @JsonPropertyDescription("Измени цвет текста для чата,число r, значение должно быть от 0 до 0.999")
        public float r;
        @JsonPropertyDescription("Измени цвет текста для чата,число g, значение должно быть от 0 до 0.999")
        public float g;
        @JsonPropertyDescription("Измени цвет текста для чата, число b, значение должно быть от 0 до 0.999")
        public float b;

    }


    public static class СhangeСolorTextResponse {
        public float r;
        public float g;
        public float b;

        public СhangeСolorTextResponse(float r, float g, float b, MyGdx mygdx) {
            this.r=r;
            this.g=g;
            this.b=b;
            mygdx.chat.cangeText(r,g,b);
            mygdx.gptText.resetTokens();
        }
    }
    OpenAiService service;
    FunctionExecutor functionExecutor;
    public List<ChatMessage> messages = new ArrayList<>();
    MyGdx mygdx;
    public int tokens;
   public GPTtext(MyGdx myGdx) {
        this.mygdx =myGdx;
        String token = System.getenv("OPENAI_TOKEN");
        service = new OpenAiService(token, Duration.ofSeconds(50));
        resetTokens();
        functionExecutor = new FunctionExecutor(Collections.singletonList(ChatFunction.builder()
                .name("get_color")
                .description("Измени цвет текста для чата, мне требуется ответ из трех чеслел r g b, значение каждой переменной " +
                        "должно варьироваться от 0 до 0.999")
                .executor(СhangeСolorText.class, w -> new СhangeСolorTextResponse(w.r,w.g, w.b,mygdx))
                .build()));
    }

    public String sendTextAndGetAnswer(String text) throws IOException, ClassNotFoundException, InvocationTargetException,
            InstantiationException, IllegalAccessException, NoSuchMethodException {

        ChatMessage msg = new ChatMessage(ChatMessageRole.USER.value(), text);
        messages.add(msg);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(messages)
                .functions(functionExecutor.getFunctions())
                //  .functionCall(ChatCompletionRequest.ChatCompletionRequestFunctionCall.of("auto"))
                .n(1)
                .maxTokens(1000)
                .logitBias(new HashMap<>())
                .build();
        ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
        messages.add(responseMessage); // don't forget to update the conversation with the latest respons


        ChatFunctionCall functionCall = responseMessage.getFunctionCall();
        if (functionCall != null) {
            System.out.println("Trying to execute " + functionCall.getName() + "...");
            functionExecutor.execute(functionCall);
        }

        String s=responseMessage.getContent();
        if(s!=null) {
            try (FileWriter writer = new FileWriter("code.txt", false)) {
                // запись всей строки
                writer.write(s);
                // запись по символам
                writer.append('\n');
                writer.flush();
                System.out.println("Answer get, write to file");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            if (parserAndCompilationJavaCode(s))mygdx.logic.createObjects();
            tokens = TikTokensUtil.tokens(TikTokensUtil.ModelEnum.GPT_3_5_TURBO_0301.getName(), messages);
            mygdx.chat.tokens.setText(tokens + "/4097");
            if (tokens > 3100) resetTokens();
        }
        return s;
    }

private boolean parserAndCompilationJavaCode(String sCode) throws IOException, ClassNotFoundException, InstantiationException,
        IllegalAccessException, NoSuchMethodException, InvocationTargetException {
     System.out.println("start parsing");
    String code="";
     int startPosition=sCode.indexOf("```java")+7;
     if(startPosition==6){
         String simport=sCode.substring(0,6);
         System.out.println(simport);
         if(simport.equals("import")){code=sCode;}
         else{
         System.out.println("code DONT finde");return false;}}
     int lastPosition=sCode.lastIndexOf("```");
     if(startPosition!=6)code=sCode.substring(startPosition,lastPosition);
    System.out.println("code finde-"+code);
    //compilation code
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
    JavaFileObject fileObject = new JavaSourceFromString("Main", code);
    Iterable<? extends JavaFileObject> compilationUnits = Collections.singletonList(fileObject);
    JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
    boolean success = task.call();
    if (success) {
        System.out.println("Compilation succesful");
        URL[] urls = new URL[]{new File("").toURI().toURL()};
        URLClassLoader classLoader = new URLClassLoader(urls);
        Class<?> cls = classLoader.loadClass("Main");
        Object obj = cls.newInstance();
        cls.getMethod("run").invoke(obj);
        System.out.println("start function run");
    } else {
        System.out.println("Compilation bed");
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            System.out.println(diagnostic.getCode());
            System.out.format("Error on line %d in %s%n", diagnostic.getLineNumber(), diagnostic.getSource().toUri());
        }
    }
    fileManager.close();
    return  true;
}
private void resetTokens(){
        messages.clear();
    ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "Привет, тебе следует придерживаться следующих инструкций." +
            "1. Когда я попрошу тебя написать код, тебе следует использовать язык java. Тебе следует прислать мне класс с названием Main, в этом классе должна отсутствовать" +
            "функция public static void main(String args[]), её следует заменить функцией run(), всю логику стоит помещать внутрь этой функции. Весь " +
            "код должен находиться в одном классе. Так же мне не требуется код, связанный с отрисовкой графики, мне нужна только логика." +
            "Так же мне не требуются пояснения к коду, комментарии в коде тоже не нужны. Когда я прошу написать код вызов функций не требуется, присылай код в обычный чат"+
            "2. Когда я попрошу изменить цвет текста для чата, мне не нужен код, мне потребуется ответ из трех чисел, r,g и b. Требуется вызывать функцию в коде."+
            "3. Во всех остальных случаяях, веди себя как обычный помошник.");
    messages.add(systemMessage);
    ChatMessage msg = new ChatMessage(ChatMessageRole.USER.value(),"Мне нужен java код который создаст 7 объектов, на координатной" +
            " плоскости размером 1920 на 1025 пикселей. Каждый объект имеет размер 50 на 50 пикселей. Расставь их в случайных местах. Потом они " +
            "должны должны двигаться влево, пока не достигнут границ, после чего следует изменить направление. 1000 циклов выполнения будет достаточно." +
            " Каждую итерецию цикла требуется записывать координаты каждого объекта в текстовый файл с именем coordinates.txt в формате Object numberObject at (x, y)," +
            " numberObject - это номер объекта. При создании объекта FileWriter значение параметра boolean следует указать как false для того что бы файл перезаписывался." +
            "Вызов функции в коде не требуется, присылай ответ в обычный чат.");
    messages.add(msg);
    try {
        String str=new String(Files.readAllBytes(Paths.get("code2.txt")));
        ChatMessage msg2 = new ChatMessage(ChatMessageRole.ASSISTANT.value(),str);
        messages.add(msg2);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    tokens = TikTokensUtil.tokens(TikTokensUtil.ModelEnum.GPT_3_5_TURBO_0301.getName(), messages);
    if(mygdx.chat!=null) mygdx.chat.tokens.setText(tokens+"/4097");
}
}
