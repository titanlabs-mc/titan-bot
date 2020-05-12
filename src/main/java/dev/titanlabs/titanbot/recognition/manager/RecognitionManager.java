package dev.titanlabs.titanbot.recognition.manager;

import com.google.common.collect.Maps;
import dev.titanlabs.titanbot.recognition.RecognitionType;
import dev.titanlabs.titanbot.recognition.types.mysql.AccessDeniedRecognition;
import dev.titanlabs.titanbot.recognition.types.mysql.NotAvailableRecognition;
import pink.zak.simplediscord.registry.Registry;

import java.util.Map;

public class RecognitionManager implements Registry {
    private Map<String, RecognitionType> recognitionTypes = Maps.newHashMap();

    @Override
    public void register() {
        this.setTypes(
                new AccessDeniedRecognition(true),
                new NotAvailableRecognition(true)
        );
    }

    private void setTypes(RecognitionType... types) {
        for (RecognitionType type : types) {
            this.recognitionTypes.put(type.getName(), type);
        }
    }

    public Map<String, RecognitionType> getEnabledTypes() {
        Map<String, RecognitionType> enabledTypes = Maps.newHashMap();
        for (RecognitionType type : this.recognitionTypes.values()) {
            if (type.isEnabled()) {
                enabledTypes.put(type.getName(), type);
            }
        }
        return enabledTypes;
    }

    public Map<String, RecognitionType> getTypes() {
        return this.recognitionTypes;
    }
}
