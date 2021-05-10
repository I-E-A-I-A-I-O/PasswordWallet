import {Request, Response, NextFunction} from 'express';
import {rsa, db, token, queries} from '../helpers';
import {CRUD} from './types/CRUD';

export class Passwords extends CRUD {
  async create(req: Request, res: Response, next: NextFunction) {
    const {authorization} = req.headers;
    const payload = await token.getPayload(authorization);
    const {description, password} = req.body;
    if (!payload) {
      return next('Auth token missing.');
    }
    const client = await db.getClient();
    try {
      const encrypted = rsa.encrypt(password);
      const addedPassword = await client.query(queries.insertPassword, [
        payload.userId,
        description,
        encrypted,
      ]);
      res.status(201).json({
        password_id: addedPassword.rows[0].password_id,
        description,
        password,
      });
    } catch (err) {
      next(err);
    } finally {
      client.release();
    }
  }
  private decryptPasswords(passwords: Password[]): Password[] {
    let decryptedPasswords: Password[] = [];
    for (let i = 0; i < passwords.length; i++) {
      decryptedPasswords.push({
        password_id: passwords[i].password_id,
        description: passwords[i].description,
        password: rsa.decrypt(passwords[i].password),
      });
    }
    return decryptedPasswords;
  }
  async read(req: Request, res: Response, next: NextFunction) {
    const {authorization} = req.headers;
    const payload = await token.getPayload(authorization);
    if (!payload) {
      return next('Auth token missing.');
    }
    const client = await db.getClient();
    try {
      const passwords = await client.query<Password>(queries.getPasswords, [
        payload.userId,
      ]);
      res.status(201).json([...this.decryptPasswords(passwords.rows)]);
    } catch (err) {
      next(err);
    } finally {
      client.release();
    }
  }
  update(req: Request, res: Response, next: NextFunction) {}
  async delete(req: Request, res: Response, next: NextFunction) {
    const {authorization} = req.headers;
    const payload = await token.getPayload(authorization);
    if (!payload) {
      return next('Auth token missing.');
    }
    const client = await db.getClient();
    const {passwordId} = req.params;
    try {
      await client.query(queries.deletePassword, [passwordId]);
      res.sendStatus(200);
    } catch (err) {
      next(err);
    } finally {
      client.release();
    }
  }
}
