const { NODE_ENV } = process.env;
module.exports = {
  SERVICE_ROOT: NODE_ENV === 'development' ? '/http_server' : '',
};
