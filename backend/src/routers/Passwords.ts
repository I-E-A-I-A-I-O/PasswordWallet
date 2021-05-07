import express, {Request, Response, NextFunction} from 'express';
import {body, validationResult} from 'express-validator';
import {passwords} from '../controllers';

export const Router = express.Router({
  strict: true,
});

Router.post(
  '/password',
  body('description').notEmpty({ignore_whitespace: true}),
  body('password').notEmpty({ignore_whitespace: true}),
  (req: Request, res: Response, next: NextFunction) => {
    const validation = validationResult(req);
    if (!validation.isEmpty()) {
      return next(validation.array({onlyFirstError: true})[0].msg);
    }
    passwords.create(req, res, next);
  }
);

Router.get('/user', (req: Request, res: Response, next: NextFunction) => {
  passwords.read(req, res, next);
});
