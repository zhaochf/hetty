package com.hetty.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

public final class ProtocolUtils {
	public static byte[] encode(Object obj) throws IOException {
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

		Hessian2Output output = new Hessian2Output(byteArray);
		output.writeObject(obj);
		
		output.close();
		
		return byteArray.toByteArray();
	}

	public static Object decode(byte[] bytes) throws IOException {
		Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));
		Object resultObject = input.readObject();
		input.close();
		return resultObject;
	}

}
