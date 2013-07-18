package nl.sidn.dnslib.logic.openssl.impl;

import org.bridj.ann.CLong;

import nl.sidn.dnslib.logic.openssl.wrapper.LibCryptoLibrary.CRYPTO_set_id_callback_func_callback;

public class IdCallback extends CRYPTO_set_id_callback_func_callback {

	@Override
	@CLong
	public long apply() {
		return Thread.currentThread().getId();
	}

}
