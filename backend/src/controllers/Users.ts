import {Request, Response, NextFunction} from 'express';
import {db, queries} from '../helpers';
import validator from 'express-validator';
import {CRUD} from './types/CRUD';

export class Users extends CRUD {
  async create(req: Request, res: Response, next: NextFunction) {
    const {email, password, confirm} = req.body;
    if (password !== confirm) {
      return res.sendStatus(400);
    }
    const client = await db.getClient();
    try {
      const createdUser = await client.query(queries.createUser, [
        email,
        password,
      ]);
      return res.status(201).json({
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
