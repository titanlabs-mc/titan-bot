package dev.titanlabs.titanbot.managers;

import com.google.common.collect.Maps;
import dev.titanlabs.titanbot.recognition.RecognitionType;
import dev.titanlabs.titanbot.recognition.types.configuration.WrongZombiePigmanRecognition;
import dev.titanlabs.titanbot.recognition.types.mysql.AccessDeniedRecognition;
import dev.titanlabs.titanbot.recognition.types.mysql.NotAvailableRecognition;
import pink.zak.simplediscord.registry.Registry;

import java.util.Map;
import java.util.Optional;

public class RecognitionManager implements Registry {
    private final Map<String, RecognitionType> recognitionTypes = Maps.newHashMap();

    @Override
    public void register() {
        this.setTypes(
                new AccessDeniedRecognition(true),
                new NotAvailableRecognition(true),
                new WrongZombiePigmanRecognition(true)
        );
    }

    public Map<String, RecognitionType> getEnabledTypes() {
        Map<String, RecognitionType> enabledTypes = Maps.newHashMap();
        for (RecognitionType type : this.recognitionTypes.values()) {
            if (type.isEnabled()) {
                enabledTypes.put(type.getIdentifier(), type);
            }
        }
        return enabledTypes;
    }

    public Optional<RecognitionType> getType(String identifier) {
        return this.recognitionTypes.containsKey(identifier) ? Optional.of(this.recognitionTypes.get(identifier)) : Optional.empty();
    }

    public Map<String, RecognitionType> getTypes() {
        return this.recognitionTypes;
    }

    private void setTypes(RecognitionType... types) {
        for (RecognitionType type : types) {
            this.recognitionTypes.put(type.getIdentifier(), type);
        }
    }
}
