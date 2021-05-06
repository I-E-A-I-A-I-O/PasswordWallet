import {Request, Response, NextFunction} from 'express';
import {db, queries} from '../helpers';
import bcrypt from 'bcrypt';
import {Session} from './types/Session';
import jwt from 'jsonwebtoken';
import {TokenPayload} from '../helpers/types/TokenPayload';

export class UserSession extends Session {
  async login(req: Request, res: Response, next: NextFunction) {
    const client = await db.getClient();
    const {email, password} = req.body;
    try {
      const user = await client.query(queries.compareEmail, [email]);
      if (user.rowCount === 0) {
        return next('Email not found.');
      }
      const same = await bcrypt.compare(password, user.rows[0].password);
      if (!same) {
        return next('Incorrect password.');
      }
      const tokenPayload: TokenPayload = {
        email: user.rows[0].email,
        userId: user.rows[0].user_id,
      };
      const token = jwt.sign(tokenPayload, process.env.JWT_SECRET!!);
      res.status(201).json({
        token,
        email: tokenPayload.email,
      });
    } catch (err) {
      next(err);
    } finally {
      client.release();
    }
  }
  async logout(req: Request, res: Response, next: NextFunction) {}
}
