export = {
  insertPassword:
    'INSERT INTO passwords(user_id, description, password) VALUES($1, $2, $3) RETURNING *',
  getPasswords: 'SELECT * FROM passwords WHERE user_id = $1',
  updatePassword:
    'UPDATE passwords SET password = $1, description = $2 WHERE password_id = $3',
  deletePassword: 'DELETE FROM passwords WHERE password_id = $1',
  createUser: 'INSERT INTO users(email, password) VALUES($1, $2) RETURNING *',
  updateEmail: 'UPDATE users SET email = $1 WHERE user_id = $2',
  updateUserPassword: 'UPDATE users SET password = $1 WHERE user_id = $2',
  deleteUser: 'DELETE FROM users WHERE user_id = $1',
  deletePasswords: 'DELETE FROM passwords WHERE user_id = $1',
  compareEmail: 'SELECT * FROM users WHERE email = $1',
  invalidateToken: 'INSERT INTO jwt(token) VALUES($1)',
  compareToken: 'SELECT * FROM jwt WHERE token = $1',
};
