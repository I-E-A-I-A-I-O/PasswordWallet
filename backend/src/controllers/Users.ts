import {Request, Response, NextFunction} from 'express';
import {db, queries} from '../helpers';
import {CRUD} from './types/CRUD';
import bcrypt from 'bcrypt';

export class Users extends CRUD {
  async create(req: Request, res: Response, next: NextFunction) {
    const {email, password, confirm} = req.body;
    if (password !== confirm) {
      return next("Passwords don't match");
    }
    const client = await db.getClient();
    try {
      const emails = await client.query(queries.compareEmail, [email]);
      if (emails.rowCount > 0) {
        return next('Email already registered!');
      }
      const salt = await bcrypt.genSalt();
      const hash = await bcrypt.hash(password, salt);
      const createdUser = await client.query(queries.createUser, [email, hash]);
      return res.status(201).json({
        token: null,
        user_id: createdUser.rows[0].user_id,
        email: createdUser.rows[0].email,
      });
    } catch (err) {
      next(err);
    } finally {
      client.release();
    }
  }
  async read(req: Request, res: Response, next: NextFunction) {}
  async update(req: Request, res: Response, next: NextFunction) {}
  async delete(req: Request, res: Response, next: NextFunction) {}
}
