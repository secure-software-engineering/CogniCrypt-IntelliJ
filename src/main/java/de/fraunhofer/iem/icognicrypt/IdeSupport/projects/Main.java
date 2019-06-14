package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import de.fraunhofer.iem.icognicrypt.IdeSupport.gradle.GradleSettings;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.nio.file.Paths;


public class Main
{

    public static void main(String[] args) throws IOException, OperationNotSupportedException
    {
        // TODO: Enable
        // System.out.print("Please enter the root path to your Android App project:");
        // var s = new Scanner(System.in);
        // var path = s.nextLine();
        String tmpPath = "C:\\Users\\lrs\\AndroidStudioProjects\\HelloWorld";
        //Configuration.GetInstance().SetProjectPath(tmpPath);

        //Path projectPath = Paths.get(Configuration.GetInstance().GetProjectPath());

        GradleSettings settings = new GradleSettings(Paths.get(tmpPath));

        System.out.println("Test123");

        while (true){
        }
    }
}
