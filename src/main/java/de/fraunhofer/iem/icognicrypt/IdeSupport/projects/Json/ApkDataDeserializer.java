package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ApkDataDeserializer extends StdDeserializer<IApkData>
{
    public ApkDataDeserializer() {
        this(null);
    }

    public ApkDataDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public IApkData deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException
    {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        ApkDataOutputType outputType = Enum.valueOf (ApkDataOutputType.class,  node.get("type").asText());
        IApkData data = new SimpleApkData();
        data.SetOutputFile(node.get("outputFile").asText());
        data.SetVersionCode(node.get("versionCode").asInt());
        data.SetVersionName(node.get("versionName").asText());
        data.SetBaseName(node.get("baseName").asText());
        data.SetFullName(node.get("fullName").asText());
        return data;
    }

    private class SimpleApkData implements IApkData
    {
        String _filePath;
        String _baseName;
        String _fullName;
        ApkDataOutputType _output;
        int _versionCode;
        String _versionName;

        @Override
        public void SetOutputFile(String relativeFilePath)
        {
            _filePath = relativeFilePath;
        }

        @Override
        public void SetBaseName(String baseName)
        {
            _baseName = baseName;
        }

        @Override
        public void SetFullName(String fullName)
        {
            _fullName = fullName;
        }

        @Override
        public void SetOutputType(ApkDataOutputType outputType)
        {
            _output = outputType;
        }

        @Override
        public void SetVersionCode(int versionCode)
        {
            _versionCode = versionCode;
        }

        @Override
        public void SetVersionName(String versionName)
        {
            _versionName = versionName;
        }

        @Override
        public String GetOutputFile()
        {
            return _filePath;
        }

        @Override
        public String GetBaseName()
        {
            return _baseName;
        }

        @Override
        public String GetFullName()
        {
            return _fullName;
        }

        @Override
        public ApkDataOutputType GetOutputType()
        {
            return _output;
        }

        @Override
        public int GetVersionCode()
        {
            return _versionCode;
        }

        @Override
        public String GetVersionName()
        {
            return _versionName;
        }
    }
}
