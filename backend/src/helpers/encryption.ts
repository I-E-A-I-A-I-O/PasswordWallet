import rsa from 'node-rsa';

export class Encryption {
  private key: rsa;

  constructor() {
    this.key = new rsa(process.env.PRIVATE_KEY!!, 'pkcs1');
  }

  encrypt(data: string): string {
    return this.key.encrypt(data, 'base64');
  }

  decrypt(data: string): string {
    return this.key.decrypt(data, 'base64');
  }
}
