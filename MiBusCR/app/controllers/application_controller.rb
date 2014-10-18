class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  # protect_from_forgery with: :exception
  #force_ssl

    private
    def restrict_access
      unless restrict_access_by_header
        render json: {message: 'Invalid API Token'}, status: 401
        return
      end
      @current_user
    end

    def restrict_access_by_header
      authenticate_with_http_token do |token|
        @current_user = User.find_by_token(token)
      end
      return true if !@current_user.nil?
    end

end
