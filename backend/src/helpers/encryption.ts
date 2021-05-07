import rsa from 'node-rsa';
import fse from 'fs-extra';

export class Encryption {
  private key: rsa;

  constructor() {
    if (process.env.PRIVATE_KEY) {
      this.key = new rsa(process.env.PRIVATE_KEY);
    } else {
      this.key = new rsa();
    }
  }

  encrypt(data: string): string {
    return this.key.encrypt(data, 'base64');
  }

  decrypt(data: string): string {
    return this.key.decrypt(data, 'utf8');
  }
}
