package nl.sidn.dnslib.logic.openssl.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import nl.sidn.dnslib.logic.openssl.wrapper.LibCryptoLibrary;
import nl.sidn.dnslib.logic.openssl.wrapper.LibCryptoLibrary.CRYPTO_set_locking_callback_func_callback;

import org.bridj.Pointer;

public class LockingCallback extends CRYPTO_set_locking_callback_func_callback {

	private List<Semaphore> locks = new ArrayList<>();
	
	public LockingCallback(){
		int lockCount = LibCryptoLibrary.CRYPTO_num_locks();
		for (int i = 0; i < lockCount; i++) {
			locks.add(new Semaphore(1));
		}
	}
	
	@Override
	public void apply(int mode, int type, Pointer<Byte> file, int line) {
		if (mode == LibCryptoLibrary.CRYPTO_LOCK){
			try {
				locks.get(type).acquire();
			} catch (InterruptedException e) {
				throw new RuntimeException("Interupted while acquiring lock", e);
			}
		} else{
			locks.get(type).release();
		}
	}

}
