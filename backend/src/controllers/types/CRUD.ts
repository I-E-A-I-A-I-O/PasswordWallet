import {Request, Response, NextFunction} from 'express';

export abstract class CRUD {
  abstract create(req: Request, res: Response, next: NextFunction): void;
  abstract read(req: Request, res: Response, next: NextFunction): void;
  abstract update(req: Request, res: Response, next: NextFunction): void;
  abstract delete(req: Request, res: Response, next: NextFunction): void;
}
