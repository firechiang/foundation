package net.foundation.mcrypto.decoder;

import org.bouncycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbiDecoder {

    private final AbiDefinition abi;

    Map<String, AbiDefinition.Entry> methodSignatures = new HashMap<>();

    public AbiDecoder(String jsonStr) {
        this.abi = AbiDefinition.fromJson(jsonStr);
        for (AbiDefinition.Entry entry : this.abi) {
            String hexEncodedMethodSignature = Hex.toHexString(entry.encodeSignature());
            String hexEncodedMethodSignaturePrefix = hexEncodedMethodSignature.substring(0, 8);
            this.methodSignatures.put(hexEncodedMethodSignature, entry);
        }
    }


    public String getMethodName(String inputData) {
        if (inputData == null || inputData.length() < 20) {
            throw new IllegalArgumentException("Can't decode invalid input '" + inputData + "'.");
        }
        String methodBytes = inputData.startsWith("0x") ? inputData.substring(2, 10) : inputData.substring(0, 8);
        if (this.methodSignatures.containsKey(methodBytes)) {
            return this.methodSignatures.get(methodBytes).name;
        }
        return null;
    }

    public DecodedFunctionCall decodeFunctionCall(String inputData) {
        if (inputData == null || (inputData.startsWith("0x") && inputData.length() < 10) || inputData.length() < 8) {
            throw new IllegalArgumentException("Can't decode invalid input '" + inputData + "'.");
        }
        String inputNoPrefix = inputData.startsWith("0x") ? inputData.substring(2) : inputData;

        String methodBytes = inputNoPrefix.substring(0, 8);

        if (!this.methodSignatures.containsKey(methodBytes)) {
            return null;
        }
        AbiDefinition.Entry abiEntry = this.methodSignatures.get(methodBytes);

        if (!(abiEntry instanceof AbiDefinition.Function)) {
            throw new IllegalArgumentException("Input data is not a function call, it's of type '" + abiEntry.type + "'.");
        }
        AbiDefinition.Function abiFunction = (AbiDefinition.Function) abiEntry;

        List<DecodedFunctionCall.Param> params = new ArrayList<>(abiFunction.inputs.size());
        List<?> decoded = abiFunction.decode(Hex.decode(inputNoPrefix));

        for (int i = 0; i < decoded.size(); i++) {
            AbiDefinition.Entry.Param paramDefinition = abiFunction.inputs.get(i);
            DecodedFunctionCall.Param param = new DecodedFunctionCall.Param(paramDefinition.getName(), paramDefinition.getType().getName(), decoded.get(i));
            params.add(param);
        }
        return new DecodedFunctionCall(abiFunction.name, params);
    }
}
