import express, {Request, Response, NextFunction} from 'express';
import {session} from '../controllers';
import {body, validationResult} from 'express-validator';

export const Router = express.Router();

Router.post(
  '/login',
  body('email').isEmail(),
  body('password').isLength({
    max: 30,
    min: 5,
  }),
  (req: Request, res: Response, next: NextFunction) => {
    const validation = validationResult(req);
    if (!validation.isEmpty) {
      return next(validation.array({onlyFirstError: true})[0].msg);
    }
    session.login(req, res, next);
  }
);
