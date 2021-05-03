import {Request, Response, NextFunction} from 'express';

export abstract class Session {
  abstract login(req: Request, res: Response, next: NextFunction): void;
  abstract logout(req: Request, res: Response, next: NextFunction): void;
}
