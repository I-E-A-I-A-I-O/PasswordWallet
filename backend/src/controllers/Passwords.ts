import {Request, Response, NextFunction} from 'express';
import {rsa, db} from '../helpers';
import {CRUD} from './types/CRUD';

export class Passwords extends CRUD {
  create(req: Request, res: Response, next: NextFunction) {}
  read(req: Request, res: Response, next: NextFunction) {}
  update(req: Request, res: Response, next: NextFunction) {}
  delete(req: Request, res: Response, next: NextFunction) {}
}
