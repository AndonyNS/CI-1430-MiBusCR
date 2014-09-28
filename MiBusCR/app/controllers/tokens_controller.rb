class TokensController < ApplicationController
  def index
    head 404
  end

  def show
    head 404
  end
  # POST /token
  # POST /token.json
  def create
    @user = User.find_by_email(params[:email])
    if !@user.nil? and @user.password == params[:password]
      render json: @user.as_json(only: [:token])
    else
      head :unauthorized
    end
  end

  def update
    head 404
  end

  def destroy
    head 404
  end
end
