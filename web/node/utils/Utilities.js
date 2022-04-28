var crypto = require('crypto');

const genRandomString = (length) => {
    return crypto.randomBytes(Math.ceil(length / 2))
        .toString('hex') /** convert to hexadecimal format */
        .slice(0, length);   /** return required number of characters */
};

const hashPasswordWithHmac = (password, salt) => {
    var passwordHash = crypto.createHmac('sha512', salt)
        .update(password)
        .digest('hex'); /** Hashing algorithm sha512 */

    return { salt, passwordHash };
};

function saltHashPassword(password) {
    var salt = genRandomString(32);
    var passwordData = hashPasswordWithHmac(password, salt);

    var passwordHash = passwordData.passwordHash;
    var passwordSalt = passwordData.salt;

    return { passwordHash, passwordSalt };
}

module.exports.genRandomString = genRandomString;
module.exports.hashPasswordWithHmac = hashPasswordWithHmac;
module.exports.saltHashPassword = saltHashPassword;