package nl.sidn.dnslib.logic.openssl.wrapper;
import org.bridj.Pointer;
import org.bridj.StructObject;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
/**
 * <i>native declaration : openssl-1.0.1e/crypto/crypto.h:279</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Library("libcrypto") 
public class crypto_ex_data_st extends StructObject {
	/** C type : stack_st_void* */
	@Field(0) 
	public Pointer<stack_st_void > sk() {
		return this.io.getPointerField(this, 0);
	}
	/** C type : stack_st_void* */
	@Field(0) 
	public crypto_ex_data_st sk(Pointer<stack_st_void > sk) {
		this.io.setPointerField(this, 0, sk);
		return this;
	}
	/** gcc is screwing up this data structure :-( */
	@Field(1) 
	public int dummy() {
		return this.io.getIntField(this, 1);
	}
	/** gcc is screwing up this data structure :-( */
	@Field(1) 
	public crypto_ex_data_st dummy(int dummy) {
		this.io.setIntField(this, 1, dummy);
		return this;
	}
	public crypto_ex_data_st() {
		super();
	}
	public crypto_ex_data_st(Pointer pointer) {
		super(pointer);
	}
}