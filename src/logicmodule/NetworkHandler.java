package logicmodule;

/**
 * @author Kerekes Zoltán
 */
public interface NetworkHandler {
	
	/**
	 * The network submodule should pass the transported {@link Opcode} object 
	 * to the destination program's logic module with this method.
	 * @param opc The network-transported object.
	 */
	void decodeOpcode(Opcode opc);
	
}
