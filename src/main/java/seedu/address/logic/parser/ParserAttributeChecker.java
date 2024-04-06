package seedu.address.logic.parser;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParserAttributeChecker {

    public static boolean duplicateAttributeCheck(Map<String, String> attributes) {
        Set<String> attributeNames = new HashSet<>();
        for (String attributeName : attributes.keySet()) {
            if (!attributeNames.add(attributeName)) {
                return true;
            }
        }
        return false;
    }
}