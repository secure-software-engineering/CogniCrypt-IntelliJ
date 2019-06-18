package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json;

import com.android.build.VariantOutput;
import com.android.ide.common.build.ApkData;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ApkDataDeserializer extends StdDeserializer<ApkData>
{
    public ApkDataDeserializer() {
        this(null);
    }

    public ApkDataDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ApkData deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException
    {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String baseName = node.get("baseName").asText();
        String fullName = node.get("fullName").asText();
        VariantOutput.OutputType outputType = Enum.valueOf (VariantOutput.OutputType.class,  node.get("type").asText());
        ApkData data = new SimpleApkData(baseName, fullName, outputType);
        data.setOutputFileName(node.get("outputFile").asText());
        data.setVersionCode(node.get("versionCode").asInt());
        data.setVersionName(node.get("versionName").asText());
        return data;
    }

    private class SimpleApkData extends ApkData
    {
        String _baseName;
        String _fullName;
        OutputType _outputType;


        public SimpleApkData(String baseName, String fullName, OutputType outputType){
            _baseName = baseName;
            _fullName = fullName;
            _outputType = outputType;
        }

        @Override
        public String getBaseName()
        {
            return _baseName;
        }

        @Override
        public String getFullName()
        {
            return _fullName;
        }

        @Override
        public OutputType getType()
        {
            return _outputType;
        }

        @Override
        public String getFilterName()
        {
            return null;
        }

        @Override
        public String getDirName()
        {
            return null;
        }
    }
}
