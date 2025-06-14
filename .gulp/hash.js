const fs = require('fs');
const path = require('path');
const crypto = require('crypto');
const ignore = require('ignore');
const { glob } = require('glob');

const outputFile = 'assets.json';
const ignoreFile = '.hashignore';

const ig = ignore();
if (fs.existsSync(ignoreFile)) {
  const rules = fs.readFileSync(ignoreFile, 'utf8');
  ig.add(rules.split('\n'));
}

function hashFile(path) {
    const contents = fs.readFileSync(path);
    const hashedContents = hash(contents)
    return Object.fromEntries([[path, hashedContents]]);
}

function hash(buffer) {
    return crypto.createHash('sha256').update(buffer).digest('hex');
}

glob('**/*', { ignore: 'node_modules/**', nodir: true })
    .then(matches => matches.filter((path) => !ig.ignores(path)))
    .then(matches => matches.map(hashFile))
    .then(matches => Object.assign(...matches))
    .then(hashes => JSON.stringify(hashes, null, 2))
    .then(hashes => fs.writeFileSync(outputFile, hashes))

// fs.glob('**/*', (err, all) => {
//     const matches = all.filter((path) => !ig.ignores(path));
//     console.log(matches);
// })
