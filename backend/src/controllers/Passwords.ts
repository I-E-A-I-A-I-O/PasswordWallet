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
  read(req: Request, res: Response, next: NextFunction) {}
  update(req: Request, res: Response, next: NextFunction) {}
  delete(req: Request, res: Response, next: NextFunction) {}
}
