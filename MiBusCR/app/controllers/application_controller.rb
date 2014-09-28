class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  # protect_from_forgery with: :exception
  force_ssl

    private
    def restrict_access
      unless restrict_access_by_params
        render json: {message: 'Invalid API Token'}, status: 401
        return
      end
      @current_user
    end

    def restrict_access_by_params
      @current_user = User.find_by_token(params[:token])

      return true if @current_user
    end

end
