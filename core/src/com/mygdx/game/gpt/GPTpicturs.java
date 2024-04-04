package com.mygdx.game.gpt;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.image.CreateImageRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.net.URL;
public class GPTpicturs {

    public void generetPicturs(String promt){
        String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(token, Duration.ofSeconds(15));

        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                //.model("dall-e-3")
                .model("babbage-002")
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .user("testing")
                .n(3)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);

        System.out.println("\nCreating Image");
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt(promt)
                .build();

        System.out.println("\nImage is located at:");
        String URL=service.createImage(request).getData().get(0).getUrl();
        System.out.println(URL);
        loadImage(URL);
    }
    private void loadImage(String URL) {
        try {
            String fileName = "google.jpg";
            BufferedImage img = ImageIO.read(new URL(URL));
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            ImageIO.write(img, "jpg", file);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
