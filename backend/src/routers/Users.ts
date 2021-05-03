import express, {Request, Response, NextFunction} from 'express';
import {users} from '../controllers';
import {body, validationResult} from 'express-validator';

export const Router = express.Router({
  strict: true,
});

Router.post(
  '/user',
  body('email').isEmail(),
  body('password').isLength({min: 5, max: 30}),
  body('confirm').isLength({min: 5, max: 30}),
  (req: Request, res: Response, next: NextFunction) => {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return next(errors.array({onlyFirstError: true})[0]);
    }
    users.create(req, res, next);
  }
);
