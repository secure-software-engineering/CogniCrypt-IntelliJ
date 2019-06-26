package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json;

import com.fasterxml.jackson.annotation.*;
import sun.util.resources.cldr.pa.CurrencyNames_pa;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"outputType", "apkData", "path"})
public class OutputJson
{
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

    @Override
    public String toString()
    {
        return String.format("Path: %s;Apk Path: %s", filePath, apkData.GetOutputFile());
    }
}
