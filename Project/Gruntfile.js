module.exports = function (grunt) {

    grunt.initConfig({

		shell: {
			//dev:{
			//	command:'node server.js'
			//}
			// запуск сервера через скрипт shell'a https://www.npmjs.com/package/grunt-shell
			options: {
                stdout: true,
                stderr: true
            },
            server: {
                command: 'java -jar target/Poliglot-1.0.jar 8081'
            }
		},

		watch: {
			fest: {
                files: ['templates/*.xml'],
                tasks: ['fest'],
                options: {
                    interrupt: true,
                    atBegin: true
                }
            },
            server: {
                files: [
                    'public_html/js/**/*.js',
                    'public_html/css/**/*.css'
                ],
                options: {
                    livereload: true
                }
            }
			// запуск watcher'a, который следит за изенениями файлов  templates/*.xml
			// и если они изменяются, то запускает таск сборки шаблонов (grunt fest)
		},

		concurrent: {
			tasks:['shell','watch']
			// одновременный запуска shell'a и watcher'a https://www.npmjs.com/package/grunt-concurrent
		},

		fest: {
            templates: {
                files: [{
                    expand: true,
                    cwd: 'templates',
                    src: '*.xml',
                    dest: 'public_html/js/tmpl'
                }],
                options: {
                    template: function (data) {
                        return grunt.template.process(
                            'define(function () { return <%= contents %> ; });',
                            {data: data}
                        );
                    }
                }
            }

        },
        qunit: {
            all: ['./public_html/tests/index.html']
        }

    });

	// подключть все необходимые модули
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-qunit');
    grunt.loadNpmTasks('grunt-concurrent');
	grunt.loadNpmTasks('grunt-shell');
	grunt.loadNpmTasks('grunt-fest');

    // результат команды grunt
    grunt.registerTask('test', ['qunit:all']);
    grunt.registerTask('default', ['concurrent']);

};