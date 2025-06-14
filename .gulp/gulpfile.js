const { default: betterAjvErrors } = require('better-ajv-errors');
const Ajv = require("ajv")
const fs = require('fs');
const gulp = require('gulp');
const jsonlint = require('gulp-jsonlint');
const path = require('path');
const through2 = require('through2');

// Define schema-to-directory mapping
const schemaMap = [
  { dir: 'presets/', schema: path.resolve('schemas/presets.schema.json') },
];

// Moves outside the .gulp/ directory
process.chdir(path.resolve('..'));

// Runs our json validator
gulp.task('validate:json', function () {
  return gulp.src(['**/*.json', '!node_modules/**'])
    .pipe(jsonlint())
    .pipe(jsonlint.reporter())
    .pipe(jsonlint.failAfterError());
});

const schemaTasks = schemaMap.map(({ dir, schema: file }) => {
  const name = dir.replace(/\W+/g, '-').replace(/^\W+|\W+$/g, '');
  const taskName = `validate:schema-${name}`;
  const schemaContent = fs.readFileSync(file, { encoding: 'utf-8' });
  const schema = JSON.parse(schemaContent);
  
  gulp.task(taskName, async () => {
    const ajv = new Ajv({allErrors: true});
    const validate = ajv.compile(schema);

    return gulp.src([`${dir}/**/*.json`], { encoding: 'utf8' })
      .pipe(through2.obj(function(file, _, cb) {
        const content = file.contents.toString();
        const data = JSON.parse(content);
        const valid = validate(data);
        cb(!valid ? betterAjvErrors(schema, data, validate.errors) : null, cb);
      }))
      .on('error', (err) => {
        console.error(err);
        // console.error(`❌ Validation failed in ${dir}:`, err.message);
        process.exit(1);
      });
  });

  return taskName;
});

// Validate based on schemas
gulp.task('validate:schema', gulp.parallel(schemaTasks));
