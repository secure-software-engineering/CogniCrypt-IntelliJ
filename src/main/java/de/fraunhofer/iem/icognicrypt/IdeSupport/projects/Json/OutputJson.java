package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.intellij.openapi.diagnostic.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"outputType", "apkData", "path"})
public class OutputJson
{
    private static final Logger logger = Logger.getInstance(OutputJson.class);

    @JsonProperty("outputType")
    private OutputType outputType;
    @JsonProperty("apkData")
    private IApkData apkData;
    @JsonProperty("path")
    private String path;
    @JsonIgnore
    private String filePath;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("outputType")
    public OutputType getOutputType() {
        return outputType;
    }

    @JsonProperty("outputType")
    public void setOutputType(OutputType outputType) {
        this.outputType = outputType;
    }

    @JsonProperty("apkData")
    public IApkData getApkData() {
        return apkData;
    }

    @JsonProperty("apkData")
    public void setApkData(IApkData apkData) {
        this.apkData = apkData;
    }

    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


    public void SetFilePath(String path){
        filePath = path;
    }

    public String GetFilePath()
    {
        return filePath;
    }

    public String GetOutputFilePath(boolean absolute)
    {
        String outputFileName = getApkData().GetOutputFile();
        if (!absolute) return outputFileName;
        return Paths.get(Paths.get(filePath).getParent().toString(), outputFileName).toString();
    }

    public static OutputJson Deserialize(String path)
    {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(IApkData.class, new ApkDataDeserializer());
        mapper.registerModule(module);
        try
        {
            OutputJson[] outputs = mapper.readValue(new File(path), OutputJson[].class);
            if (outputs.length == 1) {
                OutputJson output = outputs[0];
                output.SetFilePath(path);

                logger.info("Found and deserialized output.json: " + output);
                return outputs[0];
            }
            // TODO: I'm not sure if the Json-Array is ever filled with more than one entry. If so we need to change code here.
            throw new NotImplementedException();
        }
        catch (IOException e)
        {
            logger.info("Failed deserializing output.json: " + path);
            return null;
        }
    }

    @Override
    public String toString()
    {
        return String.format("Path: %s;Apk Path: %s", filePath, apkData.GetOutputFile());
    }
}
